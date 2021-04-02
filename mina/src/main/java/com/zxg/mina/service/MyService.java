package com.zxg.mina.service;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public interface MyService {
    /**
     * 解码
     * @param session
     * @param in
     * @param out
     * @return
     */
    boolean decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out);
}
