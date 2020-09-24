package com.zxg.security.data.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxg.security.data.dto.SysUserPageReq;
import com.zxg.security.data.entity.SysUser;
import com.zxg.security.data.vo.SysUserPageResp;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author wangjian
 */
@Repository
public interface SysUserDao extends BaseMapper<SysUser> {
    @Select("select * from sys_user where account = #{account}")
    SysUser loadUserByUsername(@Param("account") String account);

    Page<SysUserPageResp> getPageList(@Param("page") Page<SysUserPageResp> page, @Param("req") SysUserPageReq req);
}
