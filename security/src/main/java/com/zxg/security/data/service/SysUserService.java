package com.zxg.security.data.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxg.security.data.dto.SysUserAddReq;
import com.zxg.security.data.dto.SysUserPageReq;
import com.zxg.security.data.dto.SysUserUpdateReq;
import com.zxg.security.data.entity.SysUser;
import com.zxg.security.data.vo.SysUserPageResp;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 用户业务层模块<p>
 *
 * @author hepeiyun
 * @since 2019/11/4
 */
public interface SysUserService extends UserDetailsService {

    /**
     * 重置密码为123456
     */
    boolean resetPwd(Long id,String pwd);

    /**
     * 用户分页列表
     */
    Page<SysUserPageResp> getPageList(SysUserPageReq req);

    /**
     * 新增
     */
    boolean addModel(SysUserAddReq req, String pwd);

    /**
     * 编辑
     */
    boolean updateModel(SysUserUpdateReq req);

    /**
     * 主键查询
     */
    SysUser selectById(Long id);

    /**
     * 更新
     */
    boolean update(SysUser user);
}
