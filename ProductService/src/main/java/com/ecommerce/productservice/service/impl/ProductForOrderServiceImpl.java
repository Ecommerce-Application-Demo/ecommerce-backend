package com.ecommerce.productservice.service.impl;

import com.ecommerce.productservice.dto.response.ProductListingResponse;
import com.ecommerce.productservice.dto.response.SizeInfo;
import com.ecommerce.productservice.dto.response.SkuResponse;
import com.ecommerce.productservice.entity.SizeDetails;
import com.ecommerce.productservice.repository.SizeDetailsRepo;
import com.ecommerce.productservice.repository.StyleVariantRepo;
import com.ecommerce.productservice.service.declaration.ProductsForOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductForOrderServiceImpl implements ProductsForOrderService {

    @Autowired
    StyleVariantRepo styleVariantRepo;
    @Autowired
    SizeDetailsRepo sizeDetailsRepo;
    @Autowired
    ProductSearchServiceImpl productSearchService;

    @Override
    public List<ProductListingResponse> getStyles(List<String> styleIds) {
       return productSearchService.getListingPageDetails(styleVariantRepo.findStyleList(styleIds.toArray(String[]::new)));
    }

    @Override
    public List<SkuResponse> getSkus(List<String> skuIds) {
        List<SkuResponse> response = new ArrayList<>();
        List<SizeDetails> psv= sizeDetailsRepo.findBySku(skuIds.toArray(String[]::new));
        if(psv!=null && !psv.isEmpty()){
            psv.forEach(p->{
                SkuResponse skuResponse = new SkuResponse();
                skuResponse.setSkuId(p.getSkuId());
                skuResponse.setStyleName(p.getPsv_id().getStyleName());
                skuResponse.setBrandName(p.getPsv_id().getProduct().getBrand().getBrandName());
                skuResponse.setSize(p.getSize());
                skuResponse.setMrp(p.getPsv_id().getMrp());
                skuResponse.setDiscountPercentage(p.getPsv_id().getDiscountPercentage());
                skuResponse.setDiscountPercentageText(p.getPsv_id().getDiscountPercentageText());
                skuResponse.setFinalPrice(p.getPsv_id().getFinalPrice());
                skuResponse.setDefaultImage(p.getPsv_id().getImages().getImage1());
                skuResponse.setAvailableQuantity(p.getQuantity());
                if(p.getQuantity()>0){
                    skuResponse.setInStock(true);
                }

                List<SizeInfo> otherSizes = new ArrayList<>();
                p.getPsv_id().getSizeDetails().forEach(s->{
                    if(!s.getSize().equals(p.getSize()) && s.getQuantity()>0){
                        otherSizes.add(new SizeInfo(s.getSkuId(),s.getSize(),s.getQuantity()));
                    }
                });
                skuResponse.setOtherSizes(otherSizes);

                response.add(skuResponse);
            });
        }
        return response;
    }
}
