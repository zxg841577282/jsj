package com.zxg.security.data.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zxg.security.data.dto.SysRoleAddReq;
import com.zxg.security.data.dto.SysRolePageReq;
import com.zxg.security.data.entity.SysPermission;
import com.zxg.security.data.vo.SysRoleInfoResp;
import com.zxg.security.data.vo.SysRolePageResp;

import java.util.List;


public interface SysRoleService {
    IPage<SysRolePageResp> getPageList(SysRolePageReq req);

    boolean addModel(SysRoleAddReq req);

    boolean updateModel(SysRoleAddReq req);

    SysRoleInfoResp getModel(Integer id);

    List<SysPermission> getPerList(Long roleId);
}
