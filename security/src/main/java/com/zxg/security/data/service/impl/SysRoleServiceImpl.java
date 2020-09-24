package com.zxg.security.data.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxg.security.data.dto.SysRoleAddReq;
import com.zxg.security.data.dto.SysRolePageReq;
import com.zxg.security.data.entity.SysPermission;
import com.zxg.security.data.entity.SysRole;
import com.zxg.security.data.entity.SysRolePermission;
import com.zxg.security.data.mapper.SysRoleDao;
import com.zxg.security.data.service.SysRolePermissionService;
import com.zxg.security.data.service.SysRoleService;
import com.zxg.security.data.vo.SysRoleInfoResp;
import com.zxg.security.data.vo.SysRolePageResp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import other.ResultException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {
    private final SysRolePermissionService sysRolePermissionService;
    private final SysRoleDao sysRoleDao;

    @Override
    public IPage<SysRolePageResp> getPageList(SysRolePageReq req) {
        IPage<SysRolePageResp> pageList = sysRoleDao.getPageList(new Page<>(req.getPageNo(), req.getPageSize()), req);

        if (ObjectUtil.isNotEmpty(pageList.getRecords())){
            List<Long> roleIds = pageList.getRecords().stream().map(SysRole::getId).collect(Collectors.toList());

            List<SysRolePermission> sysRolePermissions = new SysRolePermission().selectList(new LambdaQueryWrapper<SysRolePermission>().in(SysRolePermission::getRoleId, roleIds));

            if (ObjectUtil.isNotEmpty(sysRolePermissions)){
                List<Long> perList = sysRolePermissions.stream().map(SysRolePermission::getPermissionId).distinct().collect(Collectors.toList());

                List<SysPermission> sysPermissions = new SysPermission().selectList(new LambdaQueryWrapper<SysPermission>().in(SysPermission::getId, perList));

                for (SysRolePageResp record : pageList.getRecords()) {

                    List<Long> collect = sysRolePermissions.stream().filter(p -> p.getRoleId().equals(record.getId())).map(SysRolePermission::getPermissionId).collect(Collectors.toList());

                    List<SysPermission> collect1 = sysPermissions.stream().filter(p -> collect.contains(p.getId())).collect(Collectors.toList());

                    record.setPermissionList(collect1);
                }
            }
        }

        return pageList;
    }

    @Override
    public boolean addModel(SysRoleAddReq req) {

        //角色名不能重复
        getByName(req.getName());

        SysRole sysRole = new SysRole(req.getName());
        boolean insert = sysRole.insert();

        if (insert){
            //新增角色权限
            sysRolePermissionService.insertByList(sysRole.getId(),req.getPermissionIds());
        }

        return insert;
    }

    @Override
    public boolean updateModel(SysRoleAddReq req) {

        SysRole sysRole = new SysRole().selectById(req.getId());
        if (!sysRole.getName().equals(req.getName())){
            sysRole.setName(req.getName());
            sysRole.updateById();
        }

        //修改关联表
        List<SysRolePermission> srpList = new SysRolePermission().selectList(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, req.getId()));

        if (ObjectUtil.isNotEmpty(srpList)){
            List<Long> oldPerList = srpList.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());

            List<Long> permissionIds = req.getPermissionIds();

            //取交集
            List<Long> collect = new ArrayList<>(permissionIds);
            collect.retainAll(oldPerList);

            //删除
            oldPerList.removeAll(collect);
            new SysRolePermission().delete(new LambdaQueryWrapper<SysRolePermission>().in(SysRolePermission::getPermissionId,oldPerList).eq(SysRolePermission::getRoleId,req.getId()));

            //新增
            permissionIds.removeAll(collect);
            sysRolePermissionService.insertByList(sysRole.getId(),permissionIds);
        }else {
            sysRolePermissionService.insertByList(sysRole.getId(),req.getPermissionIds());
        }

        return true;
    }

    @Override
    public SysRoleInfoResp getModel(Integer id) {

        SysRole sysRole = new SysRole().selectById(id);

        SysRoleInfoResp resp = new SysRoleInfoResp();

        BeanUtils.copyProperties(sysRole,resp);

        List<SysRolePermission> sysRolePermissions = new SysRolePermission().selectList(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, id));

        if (ObjectUtil.isNotEmpty(sysRolePermissions)){
            List<Long> collect = sysRolePermissions.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());

            resp.setPerList(new SysPermission().selectList(new LambdaQueryWrapper<SysPermission>().in(SysPermission::getId,collect)));
        }
        return resp;
    }

    @Override
    public List<SysPermission> getPerList(Long roleId) {
        if (roleId.equals(0l)){
            return new SysPermission().selectAll();
        }

        List<SysRolePermission> sysRolePermissions = new SysRolePermission().selectList(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId));

        if (ObjectUtil.isNotEmpty(sysRolePermissions)){

            List<Long> myPerList = sysRolePermissions.stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());

            return new SysPermission().selectList(new LambdaQueryWrapper<SysPermission>().in(SysPermission::getId,myPerList));
        }
        return new ArrayList<>();
    }

    public SysRole getByName(String name){
        SysRole sysRole = new SysRole().selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getName, name));
        if (ObjectUtil.isNotEmpty(sysRole)){ throw new ResultException("存在相同角色名"); }
        return sysRole;
    }
}
