package com.zxg.mina.handle;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: zhou_xg
 * @Date: 2021/3/30
 * @Purpose:
 */

@Component
public class MyHandle extends IoHandlerAdapter {
    Logger logger = LoggerFactory.getLogger(MyHandle.class);

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        logger.info("进来了：" + session.getRemoteAddress());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        logger.info("断开连接：" + session.getRemoteAddress());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("收到信息：" + session.getRemoteAddress() + "," + message);

        super.messageReceived(session, message);
    }



}
