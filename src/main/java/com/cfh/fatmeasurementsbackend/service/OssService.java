package com.cfh.fatmeasurementsbackend.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/21
 */

public interface OssService {
    String uploadHeadportrait2Oss(InputStream inputStream) throws IOException;

    Map<String, String> uploadBUltrasonic2Oss(InputStream inputStream) throws IOException;

    void downloadBUltrasonicFromOss(File file, String ossKey) throws IOException;
}