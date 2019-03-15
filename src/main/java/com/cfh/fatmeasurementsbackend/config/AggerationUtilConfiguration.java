package com.cfh.fatmeasurementsbackend.config;

import com.cfh.fatmeasurementsbackend.common.AggregationUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/15
 */
@Configuration
public class AggerationUtilConfiguration {

    @Bean
    public AggregationUtil aggregationUtil() {
        return new AggregationUtil();
    }
}