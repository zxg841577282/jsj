package com.zxg.mina.network;

import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.springframework.stereotype.Component;

/**
 * Mina component - 编码器
 */
@Slf4j
@Component
public class Encoder extends ProtocolEncoderAdapter {

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) {
        MyMessage<?> message1 = (MyMessage<?>) message;
        byte[] serialize = message1.serialize();
        IoBuffer buf = IoBuffer.wrap(serialize);
        out.write(buf);
    }

}
