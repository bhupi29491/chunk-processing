package com.bhupi.spring_batch.chunkprocessing.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class Product {

    private Integer productId;
    private String productName;

    //    @Pattern(regexp = "Mobile Phones|Tablets|Televisions|Sports Accessories")
    private String productCategory;

    @Min(0)
    @Max(100000)
    private Integer productPrice;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", productName='" + productName + '\'' + ", productCategory='" + productCategory + '\'' + ", productPrice=" + productPrice + '}';
    }
}
