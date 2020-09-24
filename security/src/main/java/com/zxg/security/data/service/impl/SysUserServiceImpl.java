package com.zxg.security.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxg.security.data.PermissionResp;
import com.zxg.security.data.dto.SysUserAddReq;
import com.zxg.security.data.dto.SysUserPageReq;
import com.zxg.security.data.dto.SysUserUpdateReq;
import com.zxg.security.data.entity.SysPermission;
import com.zxg.security.data.entity.SysRole;
import com.zxg.security.data.entity.SysUser;
import com.zxg.security.data.mapper.SysUserDao;
import com.zxg.security.data.service.SysPermissionService;
import com.zxg.security.data.service.SysUserService;
import com.zxg.security.data.vo.AdminLoginUserResp;
import com.zxg.security.data.vo.SysUserPageResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import other.ResultException;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户业务层模块<p>
 *
 * @author hepeiyun
 * @since 2019/11/4
 */
@Slf4j
@AllArgsConstructor
@Service("")
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService, ApplicationListener<ContextRefreshedEvent> {
    private final SysPermissionService sysPermissionService;
    private final SysUserDao sysUserDao;

    @Override
    public boolean resetPwd(Long id,String pwd) {
        if (id != null) {
            SysUser sysUser = new SysUser().selectById(id);
            if (ObjectUtil.isNotEmpty(sysUser)){
                sysUser.setPwd(pwd);
                sysUser.setUpdateTime(LocalDateTime.now());
                return sysUser.insertOrUpdate();
            }
            throw new ResultException("无此用户");
        }
        throw new ResultException("用户id不能为空");
    }

    @Override
    public Page<SysUserPageResp> getPageList(SysUserPageReq req) {
        return sysUserDao.getPageList(new Page<>(req.getPageNo(),req.getPageSize()),req);
    }

    @Override
    public boolean addModel(SysUserAddReq req, String pwd) {
        //查询角色是否存在
        SysRole sysRole = new SysRole().selectById(req.getRoleId());
        if (ObjectUtil.isEmpty(sysRole)){ throw new ResultException("角色不存在"); }

        //账户名名不能重复
        getByAccount(req.getAccount());

        SysUser sysUser = new SysUser(req,pwd);
        return sysUser.insert();
    }

    @Override
    public boolean updateModel(SysUserUpdateReq req) {
        //查询角色是否存在
        SysRole sysRole = new SysRole().selectById(req.getRoleId());
        if (ObjectUtil.isEmpty(sysRole)){ throw new ResultException("角色不存在"); }

        SysUser sysUser = new SysUser().selectById(req.getId());
        sysUser.setName(req.getName());
        sysUser.setRemark(req.getRemark());
        sysUser.setStatus(req.getStatus());

        return sysUser.updateById();
    }


    public SysUser getByAccount(String account){
        SysUser sysUser = new SysUser().selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, account));
        if (ObjectUtil.isNotEmpty(sysUser)){ throw new ResultException("存在相同用户名"); }
        return sysUser;
    }

    @Override
    public AdminLoginUserResp loadUserByUsername(String s) throws UsernameNotFoundException {
        if (StringUtils.isBlank(s)){ throw new UsernameNotFoundException("账户名不存在"); }

        SysUser sysUser;
        List<PermissionResp> perList;

        //超管用户 默认全部权限
        if ("zxg".equals(s)){
            sysUser = new SysUser(s);
            perList = sysPermissionService.perList(null);
        }else {
            sysUser = sysUserDao.loadUserByUsername(s);
            perList =  sysPermissionService.perList(sysUser.getRoleId());
        }

        return new AdminLoginUserResp(sysUser,perList);
    }

    /**
     * 项目启动加载权限至数据库，会更新角色权限关系表
     * 因此尽可能少修改权限，权限部分应与项目最后期开启
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("加载接口权限数据·············");

        //当前加载的权限
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("com.zxg.security.controller")).setScanners(new MethodAnnotationsScanner()));
        Reflections reflections2 = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("com.zxg.security.controller")).addScanners(new SubTypesScanner()).addScanners(new FieldAnnotationsScanner()));

        Set<Class<?>> classes = reflections2.getTypesAnnotatedWith(PreAuthorize.class,true);
        //扫描包内带有@RequiresPermissions注解的所有方法集合
        Set<Method> methods = reflections.getMethodsAnnotatedWith(PreAuthorize.class);

        //预设新的权限
        List<PermissionResp> faList = new ArrayList<>();
        PermissionResp resp;
        for (Class<?> clazz : classes) {
            PreAuthorize annotation = clazz.getAnnotation(PreAuthorize.class);
            Api api = clazz.getAnnotation(Api.class);

            String value = annotation.value();
            String replace = value.replace("hasAuthority('", "").replace("')", "");

            resp = new PermissionResp(Arrays.toString(api.tags()),replace,clazz.getName());
            faList.add(resp);
        }

        //新增待分配父类
        faList.add(new PermissionResp("[待分配]","test_0",""));

        //获取所有权限接口列表
        List<PreAuthorize> collect = methods.stream().map(s ->s.getAnnotation(PreAuthorize.class)).collect(Collectors.toList());

        //筛选出菜单
        List<PreAuthorize> menu = collect.stream().filter(s -> (s.value().replace("hasAuthority('", "").replace("')", "").substring(0, 4).equals("menu"))).collect(Collectors.toList());

        //循环载入各菜单的权限
        for (PreAuthorize preAuthorize : menu) {
            Optional<Method> first = methods.stream().filter(p -> p.getAnnotation(PreAuthorize.class).equals(preAuthorize)).findFirst();
            if (first.isPresent()){
                Method method = first.get();

                ApiOperation ApiOperation = method.getAnnotation(io.swagger.annotations.ApiOperation.class);

                //获取我们想要的
                String menuName = preAuthorize.value().replace("hasAuthority('", "").replace("')", "");


                resp = new PermissionResp(ApiOperation.value(),menuName,1);

                List<PermissionResp> butList = new ArrayList<>();

                //查询出按钮权限
                Set<PreAuthorize> buttonList = collect.stream().filter(p -> p.value().contains(menuName)).collect(Collectors.toSet());
                PermissionResp permissionResp;
                if (CollectionUtil.isNotEmpty(buttonList)){
                    for (PreAuthorize authorize : buttonList) {
                        Optional<Method> first2 = methods.stream().filter(p -> p.getAnnotation(PreAuthorize.class).equals(authorize)).findFirst();
                        if (first2.isPresent()){
                            Method method2 = first2.get();

                            io.swagger.annotations.ApiOperation ApiOperation2 = method2.getAnnotation(io.swagger.annotations.ApiOperation.class);

                            String butName = authorize.value().replace("hasAuthority('", "").replace("')", "");
                            permissionResp = new PermissionResp(ApiOperation2.value(),butName,2);
                            butList.add(permissionResp);
                        }
                    }
                }

                resp.setSonList(butList);

                Optional<PermissionResp> first2 = faList.stream().filter(p -> p.getClassName().equals(method.getDeclaringClass().getName())).findFirst();
                if (first2.isPresent()){

                    permissionResp = first2.get();

                    List<PermissionResp> sonList = permissionResp.getSonList();
                    sonList.add(resp);
                }else {
                    //没有父类则自动归集到待分配权限中
                    permissionResp = faList.stream().filter(p -> "[待分配]".equals(p.getName())).findFirst().get();
                    List<PermissionResp> sonList = permissionResp.getSonList();
                    sonList.add(resp);
                }
            }
        }

        //对比数据库
        contrastDB(faList);
    }

    /**
     * 新加载的权限与数据库权限对比
     * @param faList
     */
    private void contrastDB(List<PermissionResp> faList){

        //先查询数据库中已有权限
        List<PermissionResp> oldList = sysPermissionService.perList(null);

        //数据库目录List
        List<String> dbCatalogValueList = oldList.stream().map(PermissionResp::getValue).collect(Collectors.toList());
        //copy一份数据库目录List
        List<String> copyDbCatalogValueList = new ArrayList<>(dbCatalogValueList);

        //新查询出的目录List
        List<String> getNowValueList = faList.stream().map(PermissionResp::getValue).collect(Collectors.toList());
        //copy一份新查询出的目录List
        List<String> copyGetNowValueList = new ArrayList<>(getNowValueList);


        //删除父类及子类
        copyDbCatalogValueList.removeAll(getNowValueList);
        if (CollectionUtil.isNotEmpty(copyDbCatalogValueList)){
            List<SysPermission> sysPermissions = new SysPermission().selectList(new LambdaQueryWrapper<SysPermission>().in(SysPermission::getValue, copyDbCatalogValueList));
            if (ObjectUtil.isNotEmpty(sysPermissions)){
                //递归删除下级
                sysPermissionService.recursionDelete(sysPermissions.stream().map(SysPermission::getId).collect(Collectors.toList()));
            }
        }

        //编辑
        copyGetNowValueList.retainAll(dbCatalogValueList);
        if (ObjectUtil.isNotEmpty(copyGetNowValueList)){
            sysPermissionService.recursionUpdate(copyGetNowValueList, oldList,faList);
        }

        //新增父类及子类
        getNowValueList.removeAll(dbCatalogValueList);
        if (ObjectUtil.isNotEmpty(getNowValueList)){
            //递归新增
            sysPermissionService.recursionAdd(getNowValueList,faList, 0L);
        }
    }

    @Override
    public SysUser selectById(Long id) {
        if (id.equals(0L)){ throw new ResultException("当前账户位超管账户"); }
        return new SysUser().selectById(id);
    }

    @Override
    public boolean update(SysUser user) {
        user.setUpdateTime(LocalDateTime.now());
        return user.updateById();
    }
}
