package com.zxg.mina.config;

import com.zxg.mina.filter.MyLoggingFilter;
import com.zxg.mina.handle.MyHandle;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @Author: zhou_xg
 * @Date: 2021/3/30
 * @Purpose:
 */

@Configuration
//@ConditionalOnBean({ProtocolEncoder.class, ProtocolDecoder.class, IoHandler.class})
public class MinaConfig {

    /**
     * 预先配置Filter信息
     */
    @Bean
    public IoFilterChainBuilder filterChainBuilder(ProtocolEncoder encoder, ProtocolDecoder decoder) {
        final DefaultIoFilterChainBuilder filterChain = new DefaultIoFilterChainBuilder();
        filterChain.addLast("logger", new MyLoggingFilter());//自定义
        filterChain.addLast("codec", new ProtocolCodecFilter(encoder, decoder));
        filterChain.addLast("pool", new ExecutorFilter()); // default 16 threads
        return filterChain;
    }

    /**
     * 加载配置文件，作为参数传入bean类
     */
    @Configuration
    @EnableConfigurationProperties(TcpConfig.class)
    protected static class tcpConfig{
        /**
         * nioAcceptor初始化
         */
        @Bean
//        @ConditionalOnMissingBean
        public IoAcceptor nioSocketAcceptorIni(TcpConfig config,IoFilterChainBuilder filterChainBuilder){
            // 创建一个非阻塞的service端Socket Nio
            NioSocketAcceptor acceptor = new NioSocketAcceptor();

            //设置过滤器 此处使用自定义的MyLoggingFilter
            acceptor.setFilterChainBuilder(filterChainBuilder);

            //设置消息逻辑处理器 此处使用自定义的MyHandle
            acceptor.setHandler(new MyHandle());

            acceptor.getSessionConfig().setReadBufferSize(config.getReadBufferSize());// 设置读取数据缓冲区
            acceptor.setBacklog(config.getBackLog());   // 处理队列大小
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, config.getTime());// 设置读写都闲置的情况下的超时时间
            acceptor.getSessionConfig().setKeepAlive(Boolean.TRUE);//保持存活
            acceptor.getSessionConfig().setSoLinger(0);
            acceptor.getSessionConfig().setReuseAddress(Boolean.TRUE);//重新地址
            acceptor.getSessionConfig().setTcpNoDelay(Boolean.TRUE);//tcp连接不延迟

            //设置端口号
            int port = config.getPort();
            try {
                acceptor.bind(new InetSocketAddress(port));
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("minaService is running, listening port:" + port);

            return acceptor;
        }
    }
}
