package com.ecommerce.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingPageDetails{
       private List<ProductListingResponse> productList;
       private List<BreadCrumb> breadCrumbList;
       private int totalPages;
       private int currentPage;
       private long totalProductCount;
       private int currentPageProductCount;
       private boolean hasNextPage;
}
