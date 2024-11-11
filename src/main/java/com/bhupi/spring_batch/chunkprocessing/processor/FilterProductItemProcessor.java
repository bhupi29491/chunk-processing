package com.bhupi.spring_batch.chunkprocessing.processor;

import com.bhupi.spring_batch.chunkprocessing.domain.Product;
import org.springframework.batch.item.ItemProcessor;

public class FilterProductItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(Product item) throws Exception {
        System.out.println("FilterProductItemProcessor() executed..!!");
        if (item.getProductPrice() > 100) {
            return item;
        } else {
            return null;
        }
    }
}
