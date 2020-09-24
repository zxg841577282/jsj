package com.zxg.security.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zxg.security.data.entity.SysPermission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PermissionResp implements GrantedAuthority {

    private String name;

    private String value;

    private Integer type;//类型 0目录  1菜单  2按钮

    @JsonIgnore
    private String className;

    private List<PermissionResp> sonList;

    public PermissionResp(String name, String value,Integer type) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.sonList = new ArrayList<>();
    }

    public PermissionResp(String name, String value, String className) {
        this.name = name.replace("[","").replace("]","");
        this.value = value;
        this.type = 0;
        this.className = className;
        this.sonList = new ArrayList<>();
    }

    public PermissionResp(SysPermission sysPermission) {
        this.name = sysPermission.getName();
        this.value = sysPermission.getValue();
        this.sonList = new ArrayList<>();
    }

    @Override
    public String getAuthority() {



        return null;
    }
}
