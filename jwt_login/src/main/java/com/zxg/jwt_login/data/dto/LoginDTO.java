package com.zxg.jwt_login.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginDTO {

    @NotNull(message = "用户名")
    private String username;

    @NotNull(message = "密码")
    private String password;

}
