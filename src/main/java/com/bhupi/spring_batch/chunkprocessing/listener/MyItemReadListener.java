package com.bhupi.spring_batch.chunkprocessing.listener;

import com.bhupi.spring_batch.chunkprocessing.domain.Product;
import org.springframework.batch.core.ItemReadListener;

public class MyItemReadListener implements ItemReadListener<Product> {

    @Override
    public void beforeRead() {
        System.out.println("beforeRead() executed");
    }

    @Override
    public void afterRead(Product item) {
        System.out.println("afterRead() executed");
    }

    @Override
    public void onReadError(Exception ex) {
        System.out.println("onReadError() executed");
    }
}
