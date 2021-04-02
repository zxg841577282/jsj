package com.zxg.mina.service.impl;

import com.zxg.mina.service.MyService;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.springframework.stereotype.Service;

/**
 * @Author: zhou_xg
 * @Date: 2021/3/30
 * @Purpose:
 */
@Service
public class MyServiceImpl implements MyService {

    @Override
    public boolean decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) {

        System.out.println("session:" + session);
        System.out.println("in:" + in);
        System.out.println("out:" + out.toString());

        return false;
    }
}
