package com.ecommerce.productservice.service.impl;

import com.ecommerce.productservice.dto.ProductFilters;
import com.ecommerce.productservice.dto.SortBy;
import com.ecommerce.productservice.dto.request.ProductFilterReq;
import com.ecommerce.productservice.dto.response.*;
import com.ecommerce.productservice.entity.Product;
import com.ecommerce.productservice.entity.ProductStyleVariant;
import com.ecommerce.productservice.repository.ProductRepo;
import com.ecommerce.productservice.repository.ReviewRatingRepo;
import com.ecommerce.productservice.repository.StyleVariantRepo;
import com.ecommerce.productservice.service.declaration.ProductSearchService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
    ReviewRatingRepo reviewRatingRepo;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public SingleProductResponse getSingleProductDetails(String styleId, String styleName) {

        ProductStyleVariant styleVariant = styleVariantRepo.findSingleStyle(styleId, styleName);

        if (styleVariant != null) {
            SingleProductResponse res = modelMapper.map(styleVariant, SingleProductResponse.class);
            styleVariant.getSizeDetails().forEach(size -> {
                if (size.getQuantity() != null && size.getQuantity() > 0) {
                    res.setInStock(true);
                    if (size.getQuantity() <= 10)
                        res.setOnlyFewLeft(true);
                }
            });

            Product product = productRepo.findById(res.getProductId()).get();
            res.setProductDescription(product.getProductDescription());
            res.setMaterial(product.getMaterial());
            res.setGender(product.getGender());
            res.setBrand(product.getBrand());
            res.setProductAvgRating(styleVariant.getProductAvgRating().toString());
            res.setReviewCount(styleVariant.getReviewCount().toString());
            res.setDefaultImage(styleVariant.getImages().getImage1());
//            if(styleVariant.getCreatedTimeStamp().isAfter(LocalDateTime.now().minusDays(10)))
//                res.setNewlyAdded(true);
            res.set14dayReturnable(true);
            res.setCashOnDeliveryAvailable(true);
            res.setBreadCrumbList(productGetService.getBreadCrumb(product));

            List<SizeInfo> sizes = new ArrayList<>();
            if (!styleVariant.getSizeDetails().isEmpty()) {
                styleVariant.getSizeDetails().forEach(sizeDetails -> {
                    sizes.add(new SizeInfo(sizeDetails.getSkuId(), sizeDetails.getSize(), sizeDetails.getQuantity()));
                });
            }
            res.setSizes(sizes);

            ReviewRatingResponse ratingResponse = new ReviewRatingResponse();
            ratingResponse.setAllReviewAndRating(reviewRatingRepo.findAllByStyleId(styleId));
            ratingResponse.setCountPerRating(reviewRatingRepo.findRatingCountByStyleId(styleId));
            ratingResponse.setProductAvgRating(styleVariant.getProductAvgRating().toString());
            ratingResponse.setReviewCount(styleVariant.getReviewCount().toString());
            res.setReviewRating(ratingResponse);

            return res;
        }
        return new SingleProductResponse();
    }

    @Override
    public ListingPageDetails getProductListingSearchString(String searchString, ProductFilterReq productFilters, String sortBy, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        Page<ProductStyleVariant> styleVariants = null;
        if (productFilters == null)
            productFilters = new ProductFilterReq();
        List<BreadCrumb> breadCrumbs = new ArrayList<>();
        String[] searchString2;
        searchString = searchString.replaceAll("-", " ");
        if (Pattern.compile("under (\\d+)").matcher(searchString).find()) {
            searchString2 = searchString.split("under ");
            searchString = searchString2[0];
            productFilters.setMaxPrice(Integer.parseInt(searchString2[1]));
        }

        if (sortBy.equalsIgnoreCase(SortBy.Popularity.name())) {
            styleVariants = styleVariantRepo.findProductBySearchString(searchString, productFilters.getMasterCategories(),
                    productFilters.getCategories(), productFilters.getSubCategories(), productFilters.getBrands(), productFilters.getGender(),
                    productFilters.getColours(), productFilters.getSizes(), productFilters.getDiscountPercentage(),
                    productFilters.getMinPrice(), productFilters.getMaxPrice(),
                    pageRequest.withSort(Sort.Direction.DESC, "product_avg_rating"));
        } else if (sortBy.equalsIgnoreCase(SortBy.HighToLow.name())) {
            styleVariants = styleVariantRepo.findProductBySearchString(searchString, productFilters.getMasterCategories(),
                    productFilters.getCategories(), productFilters.getSubCategories(), productFilters.getBrands(), productFilters.getGender(),
                    productFilters.getColours(), productFilters.getSizes(), productFilters.getDiscountPercentage(),
                    productFilters.getMinPrice(), productFilters.getMaxPrice(),
                    pageRequest.withSort(Sort.Direction.DESC, "final_price"));
        } else if (sortBy.equalsIgnoreCase(SortBy.LowToHigh.name())) {
            styleVariants = styleVariantRepo.findProductBySearchString(searchString, productFilters.getMasterCategories(),
                    productFilters.getCategories(), productFilters.getSubCategories(), productFilters.getBrands(), productFilters.getGender(),
                    productFilters.getColours(), productFilters.getSizes(), productFilters.getDiscountPercentage(),
                    productFilters.getMinPrice(), productFilters.getMaxPrice(),
                    pageRequest.withSort(Sort.Direction.ASC, "final_price"));
        }

        if (searchString.equalsIgnoreCase("tshirts") || searchString.equalsIgnoreCase("t shirts")
                || searchString.equalsIgnoreCase("t-shirts")) {
            breadCrumbs.add(new BreadCrumb("T-Shirts For Men & Women", null));
        } else if (searchString.equalsIgnoreCase("shirts")) {
            breadCrumbs.add(new BreadCrumb("Shirts For Men & Women", null));
        } else if (searchString.equalsIgnoreCase("jeans") || searchString.equalsIgnoreCase("jean")) {
            breadCrumbs.add(new BreadCrumb("Jeans For Men & Women", null));
        } else {
            breadCrumbs.add(new BreadCrumb(searchString, null));
        }

        if (styleVariants.hasContent()) {
            return new ListingPageDetails(getListingPageDetails(styleVariants), breadCrumbs, styleVariants.getTotalPages(),
                    styleVariants.getNumber() + 1, styleVariants.getTotalElements(), styleVariants.getNumberOfElements(),
                    styleVariants.hasNext());
        }


        return new ListingPageDetails(null,breadCrumbs,0,0,0,0,false);
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
            Product product = styleVariant.getProduct();
            res.setBrandName(product.getBrand().getBrandName());
            res.setProductAvgRating(styleVariant.getProductAvgRating().toString());
            res.setReviewCount(styleVariant.getReviewCount().toString());
            res.setDefaultImage(styleVariant.getImages().getImage1());
//            if(styleVariant.getCreatedTimeStamp().isAfter(LocalDateTime.now().minusDays(10)))
//                res.setNewlyAdded(true);

            productListingResponse.add(res);
        });
        return productListingResponse;
    }

    @Override
    public ProductFilters getProductFilters(String searchString, ProductFilterReq productFilterReq) {
        String[] searchString2;
        searchString = searchString.replaceAll("-", " ");
        if (productFilterReq.getMaxPrice() == null) {
            if (Pattern.compile("under (\\d+)").matcher(searchString).find()) {
                searchString2 = searchString.split("under ");
                searchString = searchString2[0];
                productFilterReq.setMaxPrice(Integer.parseInt(searchString2[1]));
            }
        }

        Map<String, Object> filters = styleVariantRepo.findFilters(searchString, productFilterReq.getMasterCategories(),
                productFilterReq.getCategories(), productFilterReq.getSubCategories(), productFilterReq.getBrands(), productFilterReq.getGender(),
                productFilterReq.getColours(), productFilterReq.getSizes(), productFilterReq.getDiscountPercentage(),
                productFilterReq.getMinPrice(), productFilterReq.getMaxPrice());
        ProductFilters productFilters = modelMapper.map(filters, ProductFilters.class);
        Gson gson = new Gson();
        productFilters.setColours(gson.fromJson(filters.get("colours").toString(), new TypeToken<Set<Colours>>() {
        }.getType()));
        productFilters.setDiscountPercentages(gson.fromJson(filters.get("discount").toString(), new TypeToken<Set<DiscountPercentage>>() {
        }.getType()));

//        if (latestAppliedFilter != null && !latestAppliedFilter.isEmpty()) {
//            Set<String> s = latestAppliedFilter.keySet();
//            Set value = latestAppliedFilter.get(s.stream().findFirst().get());
//            if (value != null) {
//                switch (s.stream().findFirst().get()) {
//
//                    case "masterCategories":
//                        productFilters.setMasterCategories(value);
//                        break;
//                    case "categories":
//                        productFilters.setCategories(value);
//                        break;
//                    case "subCategories":
//                        productFilters.setSubCategories(value);
//                        break;
//                    case "brands":
//                        productFilters.setBrands(value);
//                        break;
//                    case "gender":
//                        productFilters.setGender(value);
//                        break;
//                    case "colours":
//                        productFilters.setColours(value);
//                        break;
//                    case "sizes":
//                        productFilters.setSizes(value);
//                        break;
//                    case "discountPercentages":
//                        productFilters.setDiscountPercentages(value);
//                        break;
//                }
//
//            }
//        }
        return productFilters;
    }

    @Override
    public List<SizeInfo> getSizes(String styleId) {
        ProductStyleVariant productStyleVariant = styleVariantRepo.findById(styleId).orElseGet(ProductStyleVariant::new);
        List<SizeInfo> sizes = new ArrayList<>();
        if (!productStyleVariant.getSizeDetails().isEmpty()) {
            productStyleVariant.getSizeDetails().forEach(sizeDetails ->
                    sizes.add(new SizeInfo(sizeDetails.getSkuId(), sizeDetails.getSize(), sizeDetails.getQuantity())));
        }
        return sizes;
    }

    @Override
    public Set<ColourInfo> getColours(String productId, String styleId) {
        if (productId == null && styleId != null)
            productId = Objects.requireNonNull(styleVariantRepo.findById(styleId).orElse(null)).getProduct().getProductId();
        List<ProductStyleVariant> productStyleVariantList = new ArrayList<>();
        if (productId != null)
            productStyleVariantList = styleVariantRepo.findStyle(productId, null);
        Set<ColourInfo> colourInfos = new HashSet<>();
        if (!productStyleVariantList.isEmpty()) {
            productStyleVariantList.forEach(psv -> {
                AtomicBoolean isInStock = new AtomicBoolean(false);
                AtomicBoolean onlyFewLeft = new AtomicBoolean(false);
                getSizes(psv.getStyleId()).forEach(size -> {
                    if (size.quantity() != null && size.quantity() > 0) {
                        isInStock.set(true);
                        if (size.quantity() <= 10)
                            onlyFewLeft.set(true);
                    }
                });
                if (isInStock.get())
                    colourInfos.add(new ColourInfo(psv.getStyleId(), psv.getStyleName(), psv.getColour(), psv.getColourHexCode(), psv.getImages().getImage1(), onlyFewLeft.get(), true));
                else
                    colourInfos.add(new ColourInfo(psv.getStyleId(), psv.getStyleName(), psv.getColour(), psv.getColourHexCode(), psv.getImages().getImage1(), onlyFewLeft.get(), false));
            });
        }
        return colourInfos;
    }

    @Override
    public ListingPageDetails getSimilarProducts(String styleId, String sortBy, Integer pageNumber, Integer productsPerPage) {
        ProductStyleVariant psv = styleVariantRepo.findById(styleId).get();
        Integer minPrice = psv.getFinalPrice().intValue() - (psv.getFinalPrice().intValue() * 50) / 100;
        Integer maxPrice = psv.getFinalPrice().intValue() + (psv.getFinalPrice().intValue() * 50) / 100;
        return getProductListingParameters(null, psv.getProduct().getCategory().getCategoryName(), null,
                null, null, null, null, null, minPrice, maxPrice,
                sortBy, pageNumber, productsPerPage);
    }


    //------------------------------------------------------------------------------------------------

    @Override
    public ListingPageDetails getProductListingParameters(String masterCategoryName, String categoryName, String subCategoryName, String brand,
                                                          String gender, String colour, String size, Integer discountPercentage,
                                                          Integer minPrice, Integer maxPrice, String sortBy, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        Page<ProductStyleVariant> styleVariants = null;

        if (sortBy.equalsIgnoreCase(SortBy.HighToLow.name())) {
            styleVariants = styleVariantRepo.findProductByParameters(masterCategoryName, categoryName, subCategoryName, brand, gender,
                    colour, size, discountPercentage, minPrice, maxPrice,
                    pageRequest.withSort(Sort.Direction.DESC, "final_price"));
        }
        if (sortBy.equalsIgnoreCase(SortBy.LowToHigh.name())) {
            styleVariants = styleVariantRepo.findProductByParameters(masterCategoryName, categoryName, subCategoryName, brand, gender,
                    colour, size, discountPercentage, minPrice, maxPrice,
                    pageRequest.withSort(Sort.Direction.ASC, "final_price"));
        }
        if (sortBy.equalsIgnoreCase(SortBy.Popularity.name())) {
            styleVariants = styleVariantRepo.findProductByParameters(masterCategoryName, categoryName, subCategoryName, brand, gender,
                    colour, size, discountPercentage, minPrice, maxPrice,
                    pageRequest.withSort(Sort.Direction.DESC, "product_avg_rating"));
        }
        if (styleVariants.hasContent()) {
            Product product = styleVariants.stream().findFirst().get().getProduct();
            return new ListingPageDetails(getListingPageDetails(styleVariants), productGetService.getBreadCrumb(product),
                    styleVariants.getTotalPages(), styleVariants.getNumber() + 1, styleVariants.getTotalElements(),
                    styleVariants.getNumberOfElements(), styleVariants.hasNext());
        }
        return new ListingPageDetails();
    }
}
