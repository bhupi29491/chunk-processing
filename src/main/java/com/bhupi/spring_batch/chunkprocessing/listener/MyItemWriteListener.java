package com.bhupi.spring_batch.chunkprocessing.listener;

import com.bhupi.spring_batch.chunkprocessing.domain.OSProduct;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class MyItemWriteListener implements ItemWriteListener<OSProduct> {

    @Override
    public void beforeWrite(Chunk<? extends OSProduct> items) {
        System.out.println("beforeWrite() executed for products " + items);
    }

    @Override
    public void afterWrite(Chunk<? extends OSProduct> items) {
        System.out.println("afterWrite() executed for products " + items);
    }

    @Override
    public void onWriteError(Exception exception, Chunk<? extends OSProduct> items) {
        System.out.println("onWriteError() executed for products " + items);
    }
}
