package com.zxg.mina.network;

import java.io.Serializable;

/**
 * @Author: zhou_xg
 * @Date: 2021/3/30
 * @Purpose:
 */

public interface MyMessage<Payload extends Serializable> extends Serializable {
    /**
     * 消息载体
     *
     * @return
     */
    Payload payload();

    /**
     * 序列化为字节数组
     * @return
     */
    byte[] serialize();
}
