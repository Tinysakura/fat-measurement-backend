package com.cfh.fatmeasurementsbackend.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */

public interface NosService {
    String uploadHeadportrait2Nos(InputStream inputStream) throws IOException;

    String uploadBUltrasonic2Nos(InputStream inputStream) throws IOException;
}