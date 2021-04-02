package com.zxg.mina.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: zhou_xg
 * @Date: 2021/3/30
 * @Purpose:
 */
@Data
@ConfigurationProperties("spring.mina.tcp")
public class TcpConfig {

    private int port;

    private int time;

    private int backLog;

    private int readBufferSize;

}
