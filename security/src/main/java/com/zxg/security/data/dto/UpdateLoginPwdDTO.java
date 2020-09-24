package com.zxg.security.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateLoginPwdDTO {

    @NotNull(message = "原密码不能为空")
    private String oldPwd;

    @NotNull(message = "新密码不能为空")
    private String newPwd;

}
