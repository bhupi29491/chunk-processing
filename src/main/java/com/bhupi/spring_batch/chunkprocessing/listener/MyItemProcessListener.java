package com.bhupi.spring_batch.chunkprocessing.listener;

import com.bhupi.spring_batch.chunkprocessing.domain.OSProduct;
import com.bhupi.spring_batch.chunkprocessing.domain.Product;
import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.OnProcessError;

public class MyItemProcessListener {

    @BeforeProcess
    public void beforeProcess(Product item) {
        System.out.println("beforeProcess() executed for product " + item.getProductId());
    }

    @AfterProcess
    public void afterProcess(Product item, OSProduct result) {
        System.out.println("afterProcess() executed for product " + item.getProductId());
    }

    @OnProcessError
    public void onProcessError(Product item, Exception e) {
        System.out.println("onProcessError() executed for product " + item.getProductId());
    }
}
