package com.productservice.repository;

import com.productservice.entity.Sku;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SkuRepo extends CrudRepository<Sku, String> {


    @Query(value = "SELECT * FROM product.sku s LEFT OUTER JOIN product.product p " +
            "ON s.sku_product = p.product_id " +
            "WHERE (?1 IS NULL OR p.product_id = CAST(?1 AS UUID)) " +
            "AND (?2 IS NULL OR s.sku_id = ?2 ) " +
            "AND (?3 IS NULL OR s.size = ?3) " +
            "AND (?4 IS NULL OR s.colour = ?4)", nativeQuery = true)
    List<Sku> findSKU(String productId,String skuId, String size,String colour);

}
