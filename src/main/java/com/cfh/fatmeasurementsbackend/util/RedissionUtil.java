package com.cfh.fatmeasurementsbackend.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/27
 */
@Slf4j
public class RedissionUtil {

    public static <R> R executorWithLock(RedissonClient redissonClient, String lockKey, Function<Object, R> function) {
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            boolean lock = rLock.tryLock(1000, TimeUnit.MILLISECONDS);
            if (lock) {
                log.info("获取到分布式锁:{}", lockKey);
                return function.apply(lockKey);
            }

        } catch (InterruptedException e) {
            log.info("发生异常，释放分布式锁:{}", lockKey);
            rLock.unlock();
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }

        return null;
    }
}