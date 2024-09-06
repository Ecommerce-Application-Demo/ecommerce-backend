package com.ecommerce.productservice.service.declaration;

import com.ecommerce.productservice.dto.response.ProductListingResponse;
import com.ecommerce.productservice.dto.response.SkuResponse;

import java.util.List;

public interface ProductsForOrderService {

    List<ProductListingResponse> getStyles(List<String> styleIds);

    List<SkuResponse> getSkus(List<String> skuIds);
}
