package com.bhupi.spring_batch.chunkprocessing.listener;

import com.bhupi.spring_batch.chunkprocessing.domain.Product;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.OnReadError;

public class MyItemReadListener {

    @BeforeRead
    public void beforeRead() {
        System.out.println("beforeRead() executed");
    }

    @AfterRead
    public void afterRead(Product item) {
        System.out.println("afterRead() executed");
    }

    @OnReadError
    public void onReadError(Exception ex) {
        System.out.println("onReadError() executed");
    }
}
