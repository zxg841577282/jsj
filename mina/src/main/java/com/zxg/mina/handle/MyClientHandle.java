package com.zxg.mina.handle;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

/**
 * @Author: zhou_xg
 * @Date: 2021/4/2
 * @Purpose:
 */
@Component
public class MyClientHandle extends IoHandlerAdapter {

    private Object sendMessage;

    public MyClientHandle(Object sm) {
        this.sendMessage = sm;
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        System.out.println("客户端创建了");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        session.write(sendMessage);
        System.out.println("客户端进来了");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        System.out.println("客户端关闭了");
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("客户端发送消息了");
        super.messageSent(session, message);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }
}
