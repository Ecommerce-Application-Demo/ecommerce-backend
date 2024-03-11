package com.productservice.service.impl;

import com.productservice.dto.*;
import com.productservice.entity.*;
import com.productservice.repository.*;
import com.productservice.service.declaration.ProductAddService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductAddServiceImpl implements ProductAddService {

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
    public void addMasterCategory(MasterCategoryDto masterCategoryDto) {
        MasterCategory masterCategory = modelMapper.map(masterCategoryDto, MasterCategory.class);
        masterCategoryRepo.save(masterCategory);
    }

    @Override
    public void addCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        categoryRepo.save(category);
    }

    @Override
    public void addSubCategory(SubCategoryDto subCategoryDto) {
        SubCategory subCategory = modelMapper.map(subCategoryDto, SubCategory.class);
        subCategoryRepo.save(subCategory);
    }

    @Override
    public void addBrand(BrandDto brandDto) {
        Brand brand = modelMapper.map(brandDto, Brand.class);
        brandRepo.save(brand);
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        Product product=modelMapper.map(productDto, Product.class);
        String avgRating=reviewRatingRepo.findAvgRating(product.getProductId()).toString();
        product.setProductAvgRating(avgRating);
        product=productRepo.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ReviewRating addReview(ReviewRating reviewRating){
        return reviewRatingRepo.save(reviewRating);
    }

    @Override
    public Sku addSku(Sku sku){
        return  skuRepo.save(sku);
    }
}
