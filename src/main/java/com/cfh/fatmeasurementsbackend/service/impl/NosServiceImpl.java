package com.cfh.fatmeasurementsbackend.service.impl;

import com.cfh.fatmeasurementsbackend.service.NosService;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.GeneratePresignedUrlRequest;
import com.netease.cloud.services.nos.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/14
 */
@Service
@Slf4j
public class NosServiceImpl implements NosService {
    @Resource(name = "nosClient")
    private NosClient nosClient;

    /**
     * 存储用户头像的文件桶
     * @param inputStream
     * @return
     */
    @Value("${nos.bucket_head_portrait}")
    String nosBucketHeadportrait;

    /**
     * 存储B超图像的文件桶
     */
    @Value("${nos.bucket_B_ultrasonic}")
    String nosBucketBUltrasonic;


    @Override
    public String uploadHeadportrait2Nos(InputStream inputStream) throws IOException{
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(inputStream.available());

        /**
         * 将用户头像放入对应的桶中
         */
        String fileName = String.valueOf(UUID.randomUUID()).concat(".jpg");
        nosClient.putObject(nosBucketHeadportrait, fileName, inputStream, objectMetadata);

        /**
         * 生成头像图片下载url
         */
        return generateNosFileUrl(nosBucketHeadportrait, fileName);
    }

    @Override
    public Map<String, String> uploadBUltrasonic2Nos(InputStream inputStream) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(inputStream.available());

        /**
         * 将B超文件放入对应的桶中
         */
        String fileName = String.valueOf(UUID.randomUUID()).concat(".BMP");
        nosClient.putObject(nosBucketBUltrasonic, fileName, inputStream, objectMetadata);

        /**
         * 生成头B超文件下载url
         */
        String url = generateNosFileUrl(nosBucketBUltrasonic, fileName);

        Map<String, String> map = new HashMap<>(2);
        map.put("nos_key", fileName);
        map.put("url", url);

        return map;
    }

    private String generateNosFileUrl(String bucketName, String key) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);

        //setExpiration为当前时间点+过期时间，设置可下载URL的过期时间。默认过期时间为1天。
        generatePresignedUrlRequest.setExpiration(new Date(System.currentTimeMillis()+3600*1000*24*30L));
        URL url = nosClient.generatePresignedUrl(generatePresignedUrlRequest);

        log.info("nos generate url:{}", url.toString());
        return url.toString();
    }
}
