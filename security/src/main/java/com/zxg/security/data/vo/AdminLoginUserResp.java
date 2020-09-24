package com.zxg.security.data.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;


import com.zxg.security.data.PermissionResp;
import com.zxg.security.data.entity.SysUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class AdminLoginUserResp implements UserDetails {
    private Long id;

    private String account;

    @JsonIgnore
    private String password;

    private Long roleId;

    private List<PermissionResp> authorities;

    public AdminLoginUserResp(SysUser sysUser, List<PermissionResp> permissionResp) {
        this.account = sysUser.getAccount();
        this.password = sysUser.getPwd();
        this.id = sysUser.getId();
        this.roleId = sysUser.getRoleId();
        authorities = permissionResp;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
