package com.cfh.fatmeasurementsbackend.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */

public interface NosService {
    String uploadHeadportrait2Nos(InputStream inputStream) throws IOException;

    Map<String, String> uploadBUltrasonic2Nos(InputStream inputStream) throws IOException;
}