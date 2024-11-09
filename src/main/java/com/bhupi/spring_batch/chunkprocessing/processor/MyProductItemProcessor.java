package com.bhupi.spring_batch.chunkprocessing.processor;

import com.bhupi.spring_batch.chunkprocessing.domain.Product;
import org.springframework.batch.item.ItemProcessor;

public class MyProductItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(Product item) throws Exception {
        System.out.println("Processor() executed");
        return item;
    }
}
