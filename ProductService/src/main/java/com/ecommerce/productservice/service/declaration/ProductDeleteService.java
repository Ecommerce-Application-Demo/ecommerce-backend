package com.ecommerce.productservice.service.declaration;

public interface ProductDeleteService {

    void deleteSize(String skuId);

    void deleteStyle(String styleId);

    void deleteProduct(String productId);
}
