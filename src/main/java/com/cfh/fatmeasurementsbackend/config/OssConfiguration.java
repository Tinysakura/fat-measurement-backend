package com.cfh.fatmeasurementsbackend.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/21
 */
@Configuration
public class OssConfiguration {
    @Value("${oss.accessKey}")
    private String ossAccessKey;

    @Value("${oss.secretKey}")
    private String ossSecretKey;

    @Value(("${oss.endPoint}"))
    private String endPoint;

    @Bean(name = "ossClient")
    public OSSClient ossClient() {
        OSSClient ossClient = new OSSClient(endPoint, ossAccessKey, ossSecretKey);

        return ossClient;
    }
}