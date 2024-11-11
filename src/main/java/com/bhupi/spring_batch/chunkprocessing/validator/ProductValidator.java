package com.bhupi.spring_batch.chunkprocessing.validator;

import com.bhupi.spring_batch.chunkprocessing.domain.Product;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.util.Arrays;
import java.util.List;

public class ProductValidator implements Validator<Product> {

    List<String> validProductCategories = Arrays.asList("Mobile Phones",
                                                        "Tablets",
                                                        "Televisions",
                                                        "Sports Accessories");

    @Override
    public void validate(Product value) throws ValidationException {
        if (!validProductCategories.contains(value.getProductCategory())) {
            throw new ValidationException(String.format("Invalid Product Category : {}", value.getProductCategory()));
        }
        if (value.getProductPrice() > 100000) {
            throw new ValidationException("Invalid Product Price");
        }
    }
}
