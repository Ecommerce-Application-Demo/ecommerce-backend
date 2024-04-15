package com.productservice.service.impl;

import com.productservice.dto.*;
import com.productservice.entity.ReviewRating;
import com.productservice.repository.*;
import com.productservice.service.declaration.ProductGetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

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
    public List<MasterCategoryDto> getMasterCategory(String masterCategoryId, String masterCategoryName) {
        List<MasterCategoryDto> masterCategoryDto = masterCategoryRepo.findMasterCategory(masterCategoryName, masterCategoryId)
                .stream().map(masterCategory -> modelMapper.map(masterCategory, MasterCategoryDto.class)).toList();
        return masterCategoryDto;
    }

    @Override
    public List<CategoryDto> getCategory(String categoryName, String categoryId, String masterCategory) {
        List<CategoryDto> categoryDto;
        if (categoryName != null || categoryId != null || masterCategory != null) {
            categoryDto = categoryRepo.findCategory(categoryName, categoryId, masterCategory).stream()
                    .map(category -> modelMapper.map(category, CategoryDto.class)).toList();
        } else {
            categoryDto = StreamSupport.stream(categoryRepo.findAll().spliterator(), false)
                    .map(category -> modelMapper.map(category, CategoryDto.class)).toList();
        }
        return categoryDto;
    }

    @Override
    public List<SubCategoryDto> getSubCategory(String subCategoryName, String subCategoryId, String categoryName) {
        List<SubCategoryDto> subCategoryDto;
        if (subCategoryName != null || subCategoryId != null || categoryName != null) {
            subCategoryDto = subCategoryRepo.findSubCategory(subCategoryName, subCategoryId, categoryName).stream()
                    .map(subCategory -> modelMapper.map(subCategory, SubCategoryDto.class)).toList();
        } else {
            subCategoryDto = StreamSupport.stream(subCategoryRepo.findAll().spliterator(), false)
                    .map(subCategory -> modelMapper.map(subCategory, SubCategoryDto.class)).toList();
        }
        return subCategoryDto;
    }

    @Override
    public List<BrandDto> getBrand() {
        List<BrandDto> brandDto = StreamSupport.stream(brandRepo.findAll().spliterator(), false)
                .map(brand -> modelMapper.map(brand, BrandDto.class)).toList();
        return brandDto;
    }

    @Override
    public List<ProductResponse> getProduct(String productId, String productName, String subCategoryName,
                                            String categoryName, String masterCategoryName, String brand, String gender) {
        List<ProductResponse> productDto;
        if (masterCategoryName != null || subCategoryName != null || categoryName != null) {
            productDto = productRepo.findProductByCategory(subCategoryName, categoryName, masterCategoryName).stream()
                    .map(product -> {
                        product.setProductAvgRating(reviewRatingRepo.findAvgRating(product.getProductId()).toString());
                        product.setReviewCount(reviewRatingRepo.findCountByProductId(product.getProductId()).toString());
                        return modelMapper.map(product, ProductDto.class);
                    }).map(productDto1 -> {
                        ProductResponse res = modelMapper.map(productDto1, ProductResponse.class);
                        res.setSku(getSku(productDto1.getProductId().toString(), null, null, null));
                        return res;
                    }).toList();
        }else {
            productDto = productRepo.findProductById_Name(productName, productId).stream()
                    .map(product -> {
                        product.setProductAvgRating(reviewRatingRepo.findAvgRating(product.getProductId()).toString());
                        product.setReviewCount(reviewRatingRepo.findCountByProductId(product.getProductId()).toString());
                         return modelMapper.map(product, ProductDto.class);
                    }).map(productDto1 -> {
                        ProductResponse res = modelMapper.map(productDto1, ProductResponse.class);
                        res.setSku(getSku(productDto1.getProductId().toString(), null, null, null));
                        return res;
                    }).toList();
        }
        return productDto;
    }

    @Override
    public List<ReviewRating> getReview(UUID productId) {
        return reviewRatingRepo.findAllByProductId(productId);
    }

    @Override
    public List<SkuDto> getSku(String productId,String skuId, String size, String colour) {

        List<SkuDto> skuDto= skuRepo.findSKU(productId,skuId,size,colour).stream()
                .map(sku -> modelMapper.map(sku,SkuDto.class)).toList();
        return skuDto;
    }
}
