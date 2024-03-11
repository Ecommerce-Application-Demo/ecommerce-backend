package com.productservice.service.impl;

import com.productservice.dto.*;
import com.productservice.entity.ReviewRating;
import com.productservice.entity.Sku;
import com.productservice.repository.*;
import com.productservice.service.declaration.ProductGetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductGetServiceImpl implements ProductGetService {
    @Autowired
    MasterCategoryRepo masterCategoryRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    SubCategoryRepo subCategoryRepo;
    @Autowired
    BrandRepo brandRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    ReviewRatingRepo reviewRatingRepo;
    @Autowired
    SkuRepo skuRepo;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public MasterCategoryDto getMasterCategory() {
        return null;
    }

    @Override
    public CategoryDto getCategory() {
        return null;
    }

    @Override
    public SubCategoryDto getSubCategory() {
        return null;
    }

    @Override
    public BrandDto getBrand() {
        return null;
    }

    @Override
    public ProductDto getProduct() {
        return null;
    }

    @Override
    public ReviewRating getReview(UUID productId) {
        return null;
    }

    @Override
    public Sku getSku(UUID productId) {
        return null;
    }
}
