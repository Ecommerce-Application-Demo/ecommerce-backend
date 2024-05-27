package com.ecommerce.productservice.service.declaration;

public interface ProductDeleteService {

    void deleteSize(String sizeId);

    void deleteStyle(String styleId);

    void deleteProduct(String productId);
}
