package com.bhupi.spring_batch.chunkprocessing.processor;

import com.bhupi.spring_batch.chunkprocessing.domain.OSProduct;
import com.bhupi.spring_batch.chunkprocessing.domain.Product;
import org.springframework.batch.item.ItemProcessor;

public class TransformProductItemProcessor implements ItemProcessor<Product, OSProduct> {

    @Override
    public OSProduct process(Product item) throws Exception {
        System.out.println("TransformProductItemProcessor() executed for product : " + item.getProductId());
        OSProduct osProduct = new OSProduct();
        osProduct.setProductId(item.getProductId());
        osProduct.setProductName(item.getProductName());
        osProduct.setProductCategory(item.getProductCategory());
        osProduct.setProductPrice(item.getProductPrice());
        osProduct.setTaxPercent(item.getProductCategory()
                                    .equals("Sports Accessories") ? 5 : 18);
        osProduct.setSku(item.getProductCategory()
                             .substring(0, 3) + item.getProductId());
        osProduct.setShippingRate(item.getProductPrice() < 1000 ? 75 : 0);
//        if (item.getProductPrice() == 500) {
//            throw new Exception("Test Exception");
//        }
        return osProduct;
    }
}
