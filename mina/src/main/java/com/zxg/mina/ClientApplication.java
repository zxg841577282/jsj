package com.zxg.mina;

import com.zxg.mina.entity.SendMessage;
import com.zxg.mina.filter.MyLoggingFilter;
import com.zxg.mina.handle.MyClientHandle;
import com.zxg.mina.network.Decoder;
import com.zxg.mina.network.Encoder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * @Author: zhou_xg
 * @Date: 2021/4/2
 * @Purpose:
 */
public class ClientApplication {

    public static void main(String[] args) {

        NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger",new MyLoggingFilter());
        connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new Encoder(), new Decoder()));

        connector.setHandler(new MyClientHandle(new SendMessage("11111哈哈哈aaa"))); //自定义连接处理类
        connector.connect(new InetSocketAddress("127.0.0.1",9999));

    }
}
