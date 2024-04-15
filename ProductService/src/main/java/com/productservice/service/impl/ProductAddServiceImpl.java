package com.productservice.service.impl;

import com.productservice.dto.*;
import com.productservice.dto.request.CategoryRequest;
import com.productservice.dto.request.ProductRequest;
import com.productservice.dto.request.SkuRequest;
import com.productservice.dto.request.SubCategoryRequest;
import com.productservice.entity.*;
import com.productservice.repository.*;
import com.productservice.service.declaration.ProductAddService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.UUID;

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
    public MasterCategoryDto addMasterCategory(MasterCategoryDto masterCategoryDto) {
        MasterCategory masterCategory = modelMapper.map(masterCategoryDto, MasterCategory.class);
        masterCategory.setMasterCategoryId(UUID.randomUUID());
         return modelMapper.map(masterCategoryRepo.save(masterCategory), MasterCategoryDto.class);
    }

    @Override
    public CategoryDto addCategory(CategoryRequest categoryRequest) {
        Category category = modelMapper.map(categoryRequest, Category.class);
        category.setCategoryId(UUID.randomUUID());
        return modelMapper.map(categoryRepo.save(category), CategoryDto.class) ;
    }

    @Override
    public SubCategoryDto addSubCategory(SubCategoryRequest subCategoryDto) {
        SubCategory subCategory = modelMapper.map(subCategoryDto, SubCategory.class);
        subCategory.setSubCategoryId(UUID.randomUUID());
        return modelMapper.map(subCategoryRepo.save(subCategory),SubCategoryDto.class);
    }

    @Override
    public BrandDto addBrand(BrandDto brandDto) {
        Brand brand = modelMapper.map(brandDto, Brand.class);
        brand.setBrandId(UUID.randomUUID());
        return modelMapper.map(brandRepo.save(brand), BrandDto.class);
    }

    @Override
    public ProductDto addProduct(ProductRequest productDto) {
        Product product=modelMapper.map(productDto, Product.class);

        product.setProductAvgRating(reviewRatingRepo.findAvgRating(product.getProductId()).toString());
        product.setReviewCount(reviewRatingRepo.findCountByProductId(product.getProductId()).toString());

        product=productRepo.save(product);
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ReviewRating addReview(ReviewRating reviewRating){
        return reviewRatingRepo.save(reviewRating);
    }

    @Override
    public Sku addSku(SkuRequest skuDto){
        Sku sku = modelMapper.map(skuDto, Sku.class);
        sku.setImages(skuDto.getImages());
        sku.setProduct(productRepo.findById(skuDto.getProductId()).get());
        sku.setSkuId( sku.getProduct().getProductId()+"_"+sku.getSize()+"_"+sku.getColour() );
        BigDecimal finalPrice = sku.getMrp().subtract(sku.getDiscountPercentage().multiply(sku.getMrp()).divide(new BigDecimal(100), MathContext.DECIMAL128));
        sku.setFinalPrice(finalPrice);

        return  skuRepo.save(sku);
    }
}
