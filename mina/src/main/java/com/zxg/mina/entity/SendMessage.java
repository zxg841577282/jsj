package com.zxg.mina.entity;

import com.zxg.mina.network.MyMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zhou_xg
 * @Date: 2021/4/2
 * @Purpose:
 */
@Data
@NoArgsConstructor
public class SendMessage implements MyMessage {

    private static final long serialVersionUID = 1L;

    private String body;

    public SendMessage(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "SendMessage{" +
                "body='" + body + '\'' +
                '}';
    }

    @Override
    public Serializable payload() {
        return null;
    }

    @Override
    public byte[] serialize() {
        byte[] body = this.body.getBytes();

        int length = body.length;

        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = body[i];
        }

        return bytes;
    }
}
