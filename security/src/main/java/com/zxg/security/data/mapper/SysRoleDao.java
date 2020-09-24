package com.zxg.security.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxg.security.data.dto.SysRolePageReq;
import com.zxg.security.data.entity.SysRole;
import com.zxg.security.data.vo.SysRolePageResp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleDao extends BaseMapper<SysRole> {
    IPage<SysRolePageResp> getPageList(@Param("page") Page<Object> objectPage, @Param("req") SysRolePageReq req);
}
