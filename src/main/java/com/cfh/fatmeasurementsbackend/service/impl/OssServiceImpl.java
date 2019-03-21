package com.cfh.fatmeasurementsbackend.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.cfh.fatmeasurementsbackend.service.OssService;
import com.sun.javafx.binding.StringFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/21
 */
@Service
public class OssServiceImpl implements OssService {

    @Resource(name = "ossClient")
    private OSSClient ossClient;

    /**
     * 存储用户头像的文件桶
     */
    @Value("${oss.bucket_head_portrait}")
    String ossBucketHeadportrait;

    /**
     * 存储B超图像的文件桶
     */
    @Value("${nos.bucket_b_ultrasonic}")
    String ossBucketBUltrasonic;

    @Value(("${oss.endPoint}"))
    private String endPoint;

    /**
     * http://<yourBucketName>.<yourEndpoint>/<yourObjectName>
     * http://<yourBucketName>.<yourEndpoint>/<yourObjectName>?x-oss-process=style/<yourStyleName>
     */
    private static String imageUrlFormat = "http://%s.%s/%s";

    @Override
    public String uploadHeadportrait2Oss(InputStream inputStream) throws IOException {
        String objectName = String.valueOf(UUID.randomUUID()).concat(".jpg");

        ossClient.putObject(ossBucketHeadportrait, objectName, inputStream);

        return generateImageUrl(ossBucketHeadportrait, endPoint, objectName);
    }

    @Override
    public Map<String, String> uploadBUltrasonic2Oss(InputStream inputStream) throws IOException {

        /**
         * 将B超文件放入对应的桶中
         */
        String objectName = String.valueOf(UUID.randomUUID()).concat(".BMP");
        ossClient.putObject(ossBucketBUltrasonic, objectName, inputStream);


        Map<String, String> map = new HashMap<>(2);
        map.put("oss_key", objectName);

        return map;
    }

    @Override
    public void downloadBUltrasonicFromOss(String downloadPath, String ossKey) throws IOException {
        File file = new File(downloadPath);

        ossClient.getObject(new GetObjectRequest(ossBucketBUltrasonic, ossKey), file);
    }

    private String generateImageUrl(String bucketName, String endPoint, String objectName) {
        return StringFormatter.format(imageUrlFormat, bucketName, endPoint, objectName).getValue();
    }
}