package com.ecommerce.productservice.service.impl;

import com.ecommerce.productservice.dto.CategoryDto;
import com.ecommerce.productservice.dto.ProductFilters;
import com.ecommerce.productservice.dto.SortBy;
import com.ecommerce.productservice.dto.SubCategoryDto;
import com.ecommerce.productservice.dto.response.*;
import com.ecommerce.productservice.entity.Brand;
import com.ecommerce.productservice.entity.MasterCategory;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.entity.ProductStyleVariant;
import com.ecommerce.productservice.repository.ProductRepo;
import com.ecommerce.productservice.repository.StyleVariantRepo;
import com.ecommerce.productservice.service.declaration.ProductSearchService;
import org.apache.commons.text.CaseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    @Autowired
    ProductRepo productRepo;
    @Autowired
    StyleVariantRepo styleVariantRepo;
    @Autowired
    ProductGetServiceImpl productGetService;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public ListingPageDetails getProductListingParameters(String masterCategoryName, String categoryName, String subCategoryName, String brand,
                                                          String gender, String colour, String size, Integer discountPercentage,
                                                          Integer minPrice, Integer maxPrice, String sortBy, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
        Page<ProductStyleVariant> styleVariants = null;

        if (sortBy.equalsIgnoreCase(SortBy.HighToLow.name())) {
            styleVariants = styleVariantRepo.getFilteredProduct(masterCategoryName, categoryName, subCategoryName, brand, gender,
                    colour, size, discountPercentage, minPrice, maxPrice,
                    SortBy.HighToLow.getQuery(), pageRequest);
        }
        if (sortBy.equalsIgnoreCase(SortBy.LowToHigh.name())) {
            styleVariants = styleVariantRepo.getFilteredProduct(masterCategoryName, categoryName, subCategoryName, brand, gender,
                    colour, size, discountPercentage, minPrice, maxPrice,
                    SortBy.LowToHigh.getQuery(), pageRequest);
        }
        if (sortBy.equalsIgnoreCase(SortBy.Popularity.name())) {
            styleVariants = styleVariantRepo.getFilteredProduct(masterCategoryName, categoryName, subCategoryName, brand, gender,
                    colour, size, discountPercentage, minPrice, maxPrice,
                    SortBy.Popularity.getQuery(), pageRequest);
        }
        if (styleVariants.hasContent()) {
            Product product = styleVariants.stream().findFirst().get().getProduct();
            return new ListingPageDetails(getListingPageDetails(styleVariants), productGetService.getBreadCrumb(product),
                    styleVariants.getTotalPages(), styleVariants.getNumber()+1,styleVariants.getTotalElements(),
                    styleVariants.getNumberOfElements(), styleVariants.hasNext());
        }
        return new ListingPageDetails();
    }

    @Override
    public ListingPageDetails getProductListingSearchString(String searchString, String sortBy, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
        Page<ProductStyleVariant> styleVariants = null;
        List<BreadCrumb> breadCrumbs = new ArrayList<>();
        String[] searchString2;
        Integer price = null;
        searchString = searchString.replaceAll("-", " ");
        if (Pattern.compile("under (\\d+)").matcher(searchString).find()) {
            searchString2 = searchString.split("under ");
            searchString = searchString2[0];
            price = Integer.parseInt(searchString2[1]);
        }

        if (sortBy.equalsIgnoreCase(SortBy.HighToLow.name())) {
            styleVariants = styleVariantRepo.findProductByField(searchString, price, SortBy.HighToLow.getQuery(), pageRequest);
        }
        if (sortBy.equalsIgnoreCase(SortBy.LowToHigh.name())) {
            styleVariants = styleVariantRepo.findProductByField(searchString, price, SortBy.LowToHigh.getQuery(), pageRequest);
        }
        if (sortBy.equalsIgnoreCase(SortBy.Popularity.name())) {
            styleVariants = styleVariantRepo.findProductByField(searchString, price, SortBy.Popularity.getQuery(), pageRequest);
        }
        if (styleVariants.hasContent()) {
            breadCrumbs.add(new BreadCrumb(CaseUtils.toCamelCase(searchString, true, ' '), null));
            return new ListingPageDetails(getListingPageDetails(styleVariants), breadCrumbs, styleVariants.getTotalPages(),
                    styleVariants.getNumber()+1,styleVariants.getTotalElements(),styleVariants.getNumberOfElements(),
                    styleVariants.hasNext());
        }
        return new ListingPageDetails();
    }

    private List<ProductListingResponse> getListingPageDetails(Page<ProductStyleVariant> styleVariants) {
        List<ProductListingResponse> productListingResponse = new ArrayList<>();

        styleVariants.forEach(styleVariant -> {
            ProductListingResponse res = modelMapper.map(styleVariant, ProductListingResponse.class);
            styleVariant.getSizeDetails().forEach(size -> {
                if (size.getQuantity() != null && size.getQuantity() > 0) {
                    res.setInStock(true);
                    if (size.getQuantity() <= 10)
                        res.setOnlyFewLeft(true);
                }
            });
            Product product = productRepo.findById(res.getProductId()).get();
            res.setBrand(product.getBrand());
            res.setProductAvgRating(product.getProductAvgRating().toString());
            res.setReviewCount(product.getReviewCount().toString());
            res.setDefaultImage(styleVariant.getImages().getImage1());
            productListingResponse.add(res);
        });
        return productListingResponse;
    }

    @Override
    public ProductFilters getProductFilters(String searchString) {
        String[] searchString2;
        Integer price = null;
        searchString = searchString.replaceAll("-", " ");
        if (Pattern.compile("under (\\d+)").matcher(searchString).find()) {
            searchString2 = searchString.split("under ");
            searchString = searchString2[0];
            price = Integer.parseInt(searchString2[1]);
        }

        return getProductFilter( styleVariantRepo.findProductFilters(searchString, price) );
    }

    @Override
    public ProductFilters getProductParameterFilter(String masterCategoryName, String categoryName, String subCategoryName, String brand, String gender, String colour, Integer discountPercentage) {

        return getProductFilter( styleVariantRepo.findProductFiltersWithParameter(masterCategoryName, categoryName, subCategoryName,
                                                    brand,gender, colour, discountPercentage) );
    }

    public ProductFilters getProductFilter(List<ProductStyleVariant> styleVariants){
        Set<MasterCategory> masterCategories = new HashSet<>();
        Set<CategoryDto> categories = new HashSet<>();
        Set<SubCategoryDto> subCategories = new HashSet<>();
        Set<Brand> brands = new HashSet<>();
        Set<ColourHexCode> colours = new HashSet<>();
        Set<String> sizes = new HashSet<>();
        Set<BigDecimal> discountPercentages = new HashSet<>();
        AtomicInteger maxPrice = new AtomicInteger(0);
        AtomicInteger minPrice = new AtomicInteger(1000000000);

        styleVariants.forEach(psv -> {
            Product product = psv.getProduct();
            masterCategories.add(product.getMasterCategory());
            categories.add(modelMapper.map(product.getCategory(), CategoryDto.class));
            if (product.getSubCategory() != null)
                subCategories.add(modelMapper.map(product.getSubCategory(), SubCategoryDto.class));
            brands.add(product.getBrand());
            colours.add(new ColourHexCode(psv.getColour(), psv.getColourHexCode()));
            psv.getSizeDetails().forEach(sizeDetail -> {
                if (sizeDetail.getSize() != null)
                    sizes.add(sizeDetail.getSize());
            });
            discountPercentages.add(psv.getDiscountPercentage());
            if (psv.getFinalPrice().intValue() > maxPrice.intValue())
                maxPrice.set(psv.getFinalPrice().intValue());
            if (psv.getFinalPrice().intValue() < minPrice.intValue())
                minPrice.set(psv.getFinalPrice().intValue());
        });

        return new ProductFilters(masterCategories, categories, subCategories, brands, colours, sizes,
                discountPercentages, BigDecimal.valueOf(maxPrice.intValue()), BigDecimal.valueOf(minPrice.intValue()));
    }


    @Override
    public List<SizeInfo> getSizes(String styleId) {
        ProductStyleVariant productStyleVariant = styleVariantRepo.findById(styleId).orElseGet(ProductStyleVariant::new);
        List<SizeInfo> sizes = new ArrayList<>();
        if (!productStyleVariant.getSizeDetails().isEmpty()) {
            productStyleVariant.getSizeDetails().forEach(sizeDetails -> {
                sizes.add(new SizeInfo(sizeDetails.getSizeId(), sizeDetails.getSize(), sizeDetails.getQuantity()));
            });
        }
        return sizes;
    }

    @Override
    public Set<ColourInfo> getColours(String productId) {
        List<ProductStyleVariant> productStyleVariantList = styleVariantRepo.findStyle(productId, null, null, null);
        Set<ColourInfo> colourInfos = new HashSet<>();
        if (!productStyleVariantList.isEmpty()) {
            productStyleVariantList.forEach(psv -> {
                AtomicBoolean flag = new AtomicBoolean(false);
                getSizes(psv.getStyleId()).forEach(size -> {
                    if (size.quantity() != null && size.quantity() > 0)
                        flag.set(true);
                });
                if (flag.get())
                    colourInfos.add(new ColourInfo(psv.getStyleId(), psv.getColour(),psv.getColourHexCode(), psv.getImages().getImage1(), true));
                else
                    colourInfos.add(new ColourInfo(psv.getStyleId(), psv.getColour(),psv.getColourHexCode(), psv.getImages().getImage1(), false));
            });
        }
        return colourInfos;
    }
}
