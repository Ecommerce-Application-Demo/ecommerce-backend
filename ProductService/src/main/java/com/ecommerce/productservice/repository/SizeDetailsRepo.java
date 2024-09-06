package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.SizeDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SizeDetailsRepo extends ListCrudRepository<SizeDetails,String> {

    @Query(value = "SELECT sd.* FROM product.size_details sd " +
            "WHERE sd.sku_id = ANY(CAST(?1 AS text[])) OR CAST(?1 AS text[]) IS NULL ", nativeQuery = true)
    List<SizeDetails> findBySku(String[] skuId);
}
