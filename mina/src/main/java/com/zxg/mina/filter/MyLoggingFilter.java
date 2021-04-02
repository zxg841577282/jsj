package com.zxg.mina.filter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;
import org.springframework.stereotype.Component;

/**
 * @Author: zhou_xg
 * @Date: 2021/3/30
 * @Purpose:
 */
@Component
public class MyLoggingFilter extends LoggingFilter {

    @Override
    public void messageReceived(NextFilter nextFilter, IoSession session, Object message) throws Exception {

        IoBuffer in = (IoBuffer) message;

        //初始化in长度的数组
        byte[] data = new byte[in.remaining()];

        //赋予data长度的in报文信息
        in.get(data);

        String str = new String(data);

        //转码
        System.out.println("解码后内容：" + str);

        super.messageReceived(nextFilter, session, str);
    }
}
