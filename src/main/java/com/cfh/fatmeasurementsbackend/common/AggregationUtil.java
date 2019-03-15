package com.cfh.fatmeasurementsbackend.common;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/2/25
 */
public class AggregationUtil<R, T> {
    private static ExecutorService executorService = Executors.newFixedThreadPool(16);

    public List<R> aggregation(List<T> keys, Function<T, R> consumer) {
        List<R> result = new ArrayList<>();


        Observable.from(keys).map(e -> executorService.submit(() -> consumer.apply(e))).map(e -> {
            try {
                return e.get();
            } catch (InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
                return null;
            }
        }).subscribe(result::add);

        return result;
    }
}