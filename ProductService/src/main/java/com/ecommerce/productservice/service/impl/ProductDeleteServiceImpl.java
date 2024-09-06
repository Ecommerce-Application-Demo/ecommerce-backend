package com.ecommerce.productservice.service.impl;

import com.ecommerce.productservice.entity.ProductStyleVariant;
import com.ecommerce.productservice.repository.InventoryRepo;
import com.ecommerce.productservice.repository.ProductRepo;
import com.ecommerce.productservice.repository.SizeDetailsRepo;
import com.ecommerce.productservice.repository.StyleVariantRepo;
import com.ecommerce.productservice.service.declaration.ProductDeleteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductDeleteServiceImpl implements ProductDeleteService {

    @Autowired
    InventoryRepo inventoryRepo;
    @Autowired
    StyleVariantRepo styleVariantRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    SizeDetailsRepo sizeDetailsRepo;

    @Override
    public void deleteSize(String skuId) {
        inventoryRepo.deleteBySkuId(skuId);
        styleVariantRepo.deleteSize(skuId);
    }

    @Override
    public void deleteStyle(String styleId) {
        ProductStyleVariant productStyleVariant = styleVariantRepo.findById(styleId).orElseGet(ProductStyleVariant::new);
        if(!productStyleVariant.getSizeDetails().isEmpty()) {
            productStyleVariant.getSizeDetails().forEach(sizeDetails -> {
                inventoryRepo.deleteBySkuId(sizeDetails.getSkuId());
                sizeDetailsRepo.delete(sizeDetails);
            });
        }
        styleVariantRepo.deleteById(styleId);
    }

    @Override
    public void deleteProduct(String productId) {
        productRepo.findById(productId).get().getProductStyleVariant()
                .forEach(psv -> deleteStyle(psv.getStyleId()));
        productRepo.deleteById(productId);
    }
}
