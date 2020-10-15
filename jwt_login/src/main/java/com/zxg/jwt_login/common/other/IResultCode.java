package com.zxg.jwt_login.common.other;

import java.io.Serializable;

public interface IResultCode extends Serializable {
    /**
     * 获取消息踢
     * @return
     */
    String getMessage();

    /**
     * 获取状态码
     * @return
     */
    int getCode();
}
