package com.bhupi.spring_batch.chunkprocessing.processor;

import com.bhupi.spring_batch.chunkprocessing.domain.Product;
import com.bhupi.spring_batch.chunkprocessing.exception.MyException;
import org.springframework.batch.item.ItemProcessor;

import java.util.Random;

public class FilterProductItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(Product item) throws Exception {
        System.out.println("FilterProductItemProcessor() executed for product " + item.getProductId());
//        if (item.getProductPrice() > 100) {
//            return item;
//        } else {
//            return null;
//        }
        Random random = new Random();
        if (item.getProductPrice() == 500 && random.nextInt(3) == 2) {
            System.out.println("Exception Thrown");
            throw new MyException("Test Exception");
        }
        return item;
    }
}
