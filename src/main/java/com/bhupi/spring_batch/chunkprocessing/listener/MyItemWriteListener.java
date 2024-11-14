package com.bhupi.spring_batch.chunkprocessing.listener;

import com.bhupi.spring_batch.chunkprocessing.domain.OSProduct;
import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.batch.item.Chunk;

public class MyItemWriteListener {

    @BeforeWrite
    public void beforeWrite(Chunk<? extends OSProduct> items) {
        System.out.println("beforeWrite() executed for products " + items);
    }

    @AfterWrite
    public void afterWrite(Chunk<? extends OSProduct> items) {
        System.out.println("afterWrite() executed for products " + items);
    }

    @OnWriteError
    public void onWriteError(Exception exception, Chunk<? extends OSProduct> items) {
        System.out.println("onWriteError() executed for products " + items);
    }
}
