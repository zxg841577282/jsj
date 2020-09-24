package com.zxg.security.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxg.security.data.PermissionResp;
import com.zxg.security.data.entity.SysPermission;
import com.zxg.security.data.entity.SysRolePermission;
import com.zxg.security.data.service.SysPermissionService;
import com.zxg.security.data.service.SysRoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {
    private final SysRoleService sysRoleService;

    @Override
    public List<PermissionResp> perList(Long roleId) {
        List<PermissionResp> backList = new ArrayList<>();
        PermissionResp resp;

        List<SysPermission> oldList;
        if (roleId!=null){
            oldList = sysRoleService.getPerList(roleId);
        }else {
            oldList = new SysPermission().selectAll();
        }


        if (ObjectUtil.isNotEmpty(oldList)){
            List<SysPermission> faList = oldList.stream().filter(p -> p.getType().equals(0)).collect(Collectors.toList());

            for (SysPermission sysPermission : faList) {
                resp = new PermissionResp(sysPermission);

                List<SysPermission> sonList = oldList.stream().filter(p -> p.getFaId().equals(sysPermission.getId())).collect(Collectors.toList());
                if (ObjectUtil.isNotEmpty(sonList)){
                    PermissionResp resp2;
                    for (SysPermission permission : sonList) {
                        resp2 = new PermissionResp(permission.getName(),permission.getValue());

                        List<SysPermission> butList = oldList.stream().filter(p -> p.getFaId().equals(permission.getId())).collect(Collectors.toList());
                        if (ObjectUtil.isNotEmpty(butList)){
                            PermissionResp resp3;
                            for (SysPermission but : butList) {
                                resp3 = new PermissionResp(but.getName(),but.getValue());
                                resp2.getSonList().add(resp3);
                            }
                        }
                        resp.getSonList().add(resp2);
                    }
                }

                backList.add(resp);
            }
        }
        return backList;
    }

    @Override
    public void recursionDelete(List<Long> perIds) {
        if (CollectionUtil.isNotEmpty(perIds)){
            List<SysPermission> sysPermissions = new SysPermission().selectList(new LambdaQueryWrapper<SysPermission>().in(SysPermission::getFaId, perIds));

            if (CollectionUtil.isNotEmpty(sysPermissions)){

                List<Long> collect = sysPermissions.stream().map(SysPermission::getId).collect(Collectors.toList());
                recursionDelete(collect);
            }else {

                //删除关系
                new SysRolePermission().delete(new LambdaQueryWrapper<SysRolePermission>().in(SysRolePermission::getPermissionId,perIds));

                //删除本体
                new SysPermission().delete(new LambdaQueryWrapper<SysPermission>().in(SysPermission::getId, perIds));
            }
        }
    }

    @Override
    public void recursionAdd(List<String> getNowValueList,List<PermissionResp> faList,Long faId) {

        if (CollectionUtil.isNotEmpty(getNowValueList)){
            List<PermissionResp> collect = faList.stream().filter(p -> getNowValueList.contains(p.getValue())).collect(Collectors.toList());
            //递归新增
            test2(collect,faId);
        }
    }

    private void test2(List<PermissionResp> collect,Long faId){

        if (CollectionUtil.isNotEmpty(collect)){

            for (PermissionResp permissionResp : collect) {
                SysPermission sysPermission = new SysPermission(permissionResp.getValue(), permissionResp.getName(),faId);
                sysPermission.insert();

                List<PermissionResp> menuList = permissionResp.getSonList();
                test2(menuList,sysPermission.getId());
            }
        }

    }

    @Override
    public void recursionUpdate(List<String> copyGetNowValueList,List<PermissionResp> oldList,List<PermissionResp> faList) {
        if (ObjectUtil.isNotEmpty(copyGetNowValueList)){
            List<String> copyList;
            List<String> copyList2;
            for (String copy : copyGetNowValueList) {
                SysPermission sysPermission = new SysPermission().selectOne(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getValue, copy));

                //判断子类是否有变化
                PermissionResp permissionResp = oldList.stream().filter(p -> p.getValue().equals(copy)).findFirst().get();
                List<PermissionResp> sonList2 = permissionResp.getSonList();

                PermissionResp permissionResp1 = faList.stream().filter(p -> p.getValue().equals(copy)).findFirst().get();
                List<PermissionResp> sonList3 = permissionResp1.getSonList();

                if (!sonList2.equals(sonList3)){

                    List<String> sonList = sonList2.stream().map(PermissionResp::getValue).collect(Collectors.toList());
                    copyList = new ArrayList<>(sonList);
                    List<String> sonList1 = sonList3.stream().map(PermissionResp::getValue).collect(Collectors.toList());
                    copyList2 = new ArrayList<>(sonList1);

                    //删除
                    copyList.removeAll(sonList1);
                    if (ObjectUtil.isNotEmpty(copyList)){
                        List<SysPermission> sysPermissions = new SysPermission().selectList(new LambdaQueryWrapper<SysPermission>().in(SysPermission::getValue, copyList));
                        if (ObjectUtil.isNotEmpty(sysPermissions)){
                            //递归删除下级
                            recursionDelete(sysPermissions.stream().map(SysPermission::getId).collect(Collectors.toList()));
                        }
                    }

                    //编辑
                    copyList2.retainAll(sonList);
                    if (ObjectUtil.isNotEmpty(copyList2)){
                        recursionUpdate(copyList2, sonList2,sonList3);
                    }

                    //新增
                    sonList1.removeAll(sonList);
                    if (ObjectUtil.isNotEmpty(sonList1)){
                        //递归新增
                        recursionAdd(sonList1,sonList3,sysPermission.getId());
                    }
                }
            }
        }
    }
}
