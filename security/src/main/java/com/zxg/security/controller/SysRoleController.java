package com.zxg.security.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxg.security.data.dto.SysRoleAddReq;
import com.zxg.security.data.dto.SysRolePageReq;
import com.zxg.security.data.entity.SysRole;
import com.zxg.security.data.entity.SysRolePermission;
import com.zxg.security.data.entity.SysUser;
import com.zxg.security.data.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import other.Group;
import other.R;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/role")
@AllArgsConstructor
@Api(tags = "角色管理")
@PreAuthorize("hasAuthority('sysrole')")
public class SysRoleController {
    private final SysRoleService sysRoleService;

    @PostMapping("/getPageList")
    @ApiOperation(value = "获取分页列表")
    @PreAuthorize("hasAuthority('menu_sysrole_list')")
    public R getPageList(@RequestBody SysRolePageReq req) {
        return R.ok(sysRoleService.getPageList(req));
    }

    @GetMapping("/getModel/{id}")
    @ApiOperation(value = "查询角色信息")
    @PreAuthorize("hasAuthority('but_menu_sysrole_list_read')")
    public R getModel(Integer id) {
        return R.ok(sysRoleService.getModel(id));
    }

    @PostMapping("/addModel")
    @ApiOperation(value = "新增")
    @PreAuthorize("hasAuthority('but_menu_sysrole_list_add')")
    public R addModel(@RequestBody SysRoleAddReq req) {
        return R.status(sysRoleService.addModel(req));
    }

    @PutMapping("/updateModel")
    @ApiOperation(value = "编辑")
    @PreAuthorize("hasAuthority('but_menu_sysrole_list_update')")
    public R updateModel(@RequestBody @Group(Update.class) SysRoleAddReq req) { ;
        return R.status(sysRoleService.updateModel(req));
    }

    @DeleteMapping("/deleteModel")
    @ApiOperation(value = "删除")
    @PreAuthorize("hasAuthority('but_menu_sysrole_list_delete')")
    public R deleteModel(@RequestBody List<Long> req) {

        //判断是否有账户使用当前角色
        List<SysUser> sysUsers = new SysUser().selectList(new LambdaQueryWrapper<SysUser>().in(SysUser::getRoleId, req));
        if (ObjectUtil.isNotEmpty(sysUsers)){ return R.error("存在账户正在使用所删除角色"); }

        if (CollectionUtil.isNotEmpty(req)){
            //删除关系表
            boolean delete = new SysRolePermission().delete(new LambdaQueryWrapper<SysRolePermission>().in(SysRolePermission::getRoleId,req));
            if (!delete){return R.error("删除关系表失败");}

            //删除角色
            boolean delete1 = new SysRole().delete(new LambdaQueryWrapper<SysRole>().in(SysRole::getId, req));

            return R.status(delete1);
        }
        return R.error("请选择对象");
    }

    @PostMapping("/getTestPage")
    @ApiOperation(value = "测试菜单")
    @PreAuthorize("hasAuthority('menu_test_list')")
    public R getPageList() {
        return R.ok();
    }

    @PostMapping("/getTestBut")
    @ApiOperation(value = "测试按钮")
    @PreAuthorize("hasAuthority('but_menu_test_list_add')")
    public R getTestBut() {
        return R.ok();
    }


}
