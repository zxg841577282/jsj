package com.zxg.jwt_login.common.other;

public enum ResultCode implements IResultCode {
    SUCCESS(200, "操作成功"),
    FAILURE(400, "业务异常"),
    CLIENT_UN_CARRIED_VOUCHER(40001, "客户端未携带凭证"),
    INVALID_CREDENTIAL(40002, "携带无效凭证"),
    LOGIN_OUT(40003, "登陆过期"),
    INVALID_USERNAME_OR_PASSWORD(40004, "无效用户名或密码"),
    balance_is_enough(201, "余额不足")


    ;



    final int code;
    final String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    private ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
