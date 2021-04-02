package com.zxg.mina.network;

import com.zxg.mina.service.MyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Mina component - 解码器
 */
@Slf4j
@Component
public final class Decoder extends CumulativeProtocolDecoder {

    @Resource
    MyService myService; // 协议处理组件, 依赖不同协议实现

    /**
     * @param session 表示客户端
     * @param in
     * @param out
     * @return
     */
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) {
        return myService.decode(session, in, out);
    }

}
