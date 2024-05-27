package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.ProductStyleVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StyleVariantRepo extends JpaRepository<ProductStyleVariant, String> {


    @Query(value = "SELECT s.* FROM product.product_style_variant s " +
            "LEFT OUTER JOIN product.product p ON s.psv_product = p.product_id " +
            "LEFT OUTER JOIN product.size_details sd ON s.style_id = sd.psv_id "+
            "WHERE (?1 IS NULL OR p.product_id = ?1) " +
            "AND (?2 IS NULL OR s.style_id = ?2 ) " +
            "AND (?3 IS NULL OR sd.size = ?3)"+
            "AND (?4 IS NULL OR s.colour = ?4) " +
            "GROUP BY s.style_id", nativeQuery = true)
    List<ProductStyleVariant> findStyle(String productId, String styleId, String Size, String colour);


    @Modifying
    @Query(value = "DELETE FROM product.size_details sd WHERE sd.size_id = ?1",nativeQuery = true)
    void deleteSize(String sizeId);


    @Query(value = "SELECT psv.* " +
            "FROM product.product_style_variant psv " +
            "INNER JOIN product.product p ON psv.psv_product = p.product_id "+
            "WHERE to_tsvector('english', p.product_name || ' ' || p.product_category || ' ' || p.product_master_category || " +
            "COALESCE(p.product_sub_category,'') || ' ' || p.product_brand || ' ' || p.material || ' ' || p.gender || ' ' || p.product_description " +
            "|| ' ' || COALESCE(psv.colour,'') ) " +
            "@@ plainto_tsquery('english', ?1) " +
            "AND (?2 IS NULL OR psv.final_price <= ?2) " +
            "ORDER BY ?3",nativeQuery = true)
    Page<ProductStyleVariant> findProductByField(String searchInput, Integer price, String sortBy, PageRequest pageRequest);

    @Query(value = "SELECT DISTINCT * FROM (SELECT s.* FROM product.product_style_variant s " +
            "INNER JOIN product.product p ON s.psv_product = p.product_id " +
            "INNER JOIN product.size_details sd ON s.style_id = sd.psv_id "+
            "WHERE (p.product_master_category = ?1 OR ?1 IS NULL) "+
            "AND (p.product_category = ?2 OR ?2 IS NULL) " +
            "AND (p.product_sub_category = ?3 OR ?3 IS NULL) " +
            "AND (p.product_brand = ?4 OR ?4 IS NULL) " +
            "AND (p.gender = ?5 OR ?5 IS NULL) " +
            "AND (s.colour = ?6 OR ?6 IS NULL) " +
            "AND (sd.size = ?7 OR ?7 IS NULL) " +
            "AND (s.discount_percentage >= ?8 OR ?8 IS NULL) " +
            "AND (s.final_price >= ?9 OR ?9 IS NULL) " +
            "AND (s.final_price <= ?10 OR ?10 IS NULL) " +
            "ORDER BY ?11)",nativeQuery = true)
    Page<ProductStyleVariant> getFilteredProduct(String masterCategoryName, String categoryName, String subCategoryName,
                                                 String brandName, String gender, String colour, String size, Integer discountPercentage,
                                                 Integer minPrice, Integer maxPrice, String sortBy, PageRequest pageRequest);

    @Query(value = "SELECT psv.* " +
            "FROM product.product_style_variant psv " +
            "INNER JOIN product.product p ON psv.psv_product = p.product_id "+
            "WHERE to_tsvector('english', p.product_name || ' ' || p.product_category || ' ' || p.product_master_category || " +
            "COALESCE(p.product_sub_category,'') || ' ' || p.product_brand || ' ' || p.material || ' ' || p.gender || ' ' || p.product_description " +
            "|| ' ' || COALESCE(psv.colour,'') ) " +
            "@@ plainto_tsquery('english', ?1) " +
            "AND (?2 IS NULL OR psv.final_price <= ?2) ",nativeQuery = true)
    List<ProductStyleVariant> findProductFilters(String searchInput,Integer price);


    @Query(value = "SELECT DISTINCT s.* FROM product.product_style_variant s " +
            "LEFT OUTER JOIN product.product p ON s.psv_product = p.product_id " +
            "LEFT OUTER JOIN product.size_details sd ON s.style_id = sd.psv_id "+
            "WHERE (p.product_master_category = ?1 OR ?1 IS NULL) "+
            "AND (p.product_category = ?2 OR ?2 IS NULL) " +
            "AND (p.product_sub_category = ?3 OR ?3 IS NULL) " +
            "AND (p.product_brand = ?4 OR ?4 IS NULL) " +
            "AND (p.gender = ?5 OR ?5 IS NULL) " +
            "AND (s.colour = ?6 OR ?6 IS NULL) " +
            "AND (s.discount_percentage >= ?7 OR ?7 IS NULL)",nativeQuery = true)
    List<ProductStyleVariant> findProductFiltersWithParameter(String masterCategoryName, String categoryName, String subCategoryName,
                                                              String brand, String gender, String colour, Integer discountPercentage);
}
