package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepo extends CrudRepository<Product, String> {

    @Query(value = "SELECT p.* " +
            "FROM product.product p " +
            "INNER JOIN product.category c ON p.product_category = c.category_name " +
            "LEFT JOIN product.sub_category sc ON p.product_sub_category = sc.sub_category_name " +
            "LEFT JOIN product.master_category mc ON (c.category_master_category = mc.master_category_name " +
            "OR sc.sub_category_master_category = mc.master_category_name) " +
            "LEFT JOIN product.brand b ON p.product_brand = b.brand_name " +
            "WHERE (c.category_name = ?2 OR ?2 IS NULL) " +
            "AND (sc.sub_category_name = ?1 OR ?1 IS NULL) " +
            "AND (mc.master_category_name = ?3 OR ?3 IS NULL) "+
            "AND (b.brand_name = ?4 OR ?4 IS NULL) " +
            "AND (p.gender = ?5 OR ?5 IS NULL)",nativeQuery = true)
    List<Product> findProductByCategory(String subCategoryName, String categoryName, String masterCategoryName,
                                        String brandName, String gender);

    @Query(value = "SELECT * " +
            "FROM product.product " +
            "WHERE (?1 IS NULL OR product_name = ?1 ) " +
            "AND (?2 IS NULL OR product_id = ?2) ", nativeQuery = true)
    List<Product> findProductById_Name(String productName, String productId);

}
