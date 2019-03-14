package com.cfh.fatmeasurementsbackend.config;

import com.netease.cloud.ClientConfiguration;
import com.netease.cloud.Protocol;
import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * NosClient配置类
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@Configuration
public class NosClientConfiguration {

    @Value("${nos_accessKey}")
    private String nosAccessKey;

    @Value("${nos_secretKey}")
    private String nosSecretKey;

    @Value(("${nos_endPoint}"))
    private String endPoint;


    @Bean(value = "nosClient")
    public NosClient nosClient() {
        Credentials credentials = new BasicCredentials(nosAccessKey, nosSecretKey);

        ClientConfiguration conf = new ClientConfiguration();
        // 设置 NosClient 使用的最大连接数
        conf.setMaxConnections(200);
        // 设置 socket 超时时间
        conf.setSocketTimeout(10000);
        // 设置失败请求重试次数
        conf.setMaxErrorRetry(2);

        NosClient nosClient = new NosClient(credentials, conf);
        nosClient.setEndpoint(endPoint);

        return nosClient;
    }

}