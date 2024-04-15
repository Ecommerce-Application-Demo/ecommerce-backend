package com.productservice.repository;

import com.productservice.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepo extends CrudRepository<Product, UUID> {

    @Query(value = "SELECT p.* " +
            "FROM product.product p " +
            "INNER JOIN product.category c ON p.product_category = c.category_name " +
            "LEFT JOIN product.sub_category sc ON p.product_sub_category = sc.sub_category_name " +
            "LEFT JOIN product.master_category mc ON (c.category_master_category = mc.master_category_name " +
            "OR sc.sub_category_master_category = mc.master_category_name) " +
            "WHERE (c.category_name = ?2 OR ?2 IS NULL) " +
            "AND (sc.sub_category_name = ?1 OR ?1 IS NULL) " +
            "AND (mc.master_category_name = ?3 OR ?3 IS NULL)",nativeQuery = true)
    List<Product> findProductByCategory(String subCategoryName, String categoryName, String masterCategoryName);

    @Query(value = "SELECT * " +
            "FROM product.product " +
            "WHERE (?1 IS NULL OR product_name = ?1 ) " +
            "AND (?2 IS NULL OR product_id = CAST(?2 AS UUID)) ", nativeQuery = true)
    List<Product> findProductById_Name(String productName, String productId);

}
