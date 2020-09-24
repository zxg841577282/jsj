package com.zxg.security.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysUserUpdateReq {

    private Long id;
    private String name;
    private String remark;
    private Integer status;

    private Long roleId;

}
