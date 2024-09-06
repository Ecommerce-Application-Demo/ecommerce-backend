package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.ProductStyleVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface StyleVariantRepo extends JpaRepository<ProductStyleVariant, String> {

    @Query(value = "SELECT s.* FROM product.product_style_variant s " +
            "WHERE s.style_id = ?1 " +
            "AND LOWER(s.style_name) = LOWER(?2) ", nativeQuery = true)
    ProductStyleVariant findSingleStyle(String styleId, String styleName);

    @Query(value = "SELECT s.* FROM product.product_style_variant s " +
            "WHERE s.style_id = ANY(CAST(?1 AS text[])) OR CAST(?1 AS text[]) IS NULL ", nativeQuery = true)
    List<ProductStyleVariant> findStyleList(String[] styleId);

    @Query(value = "SELECT s.* FROM product.product_style_variant s " +
            "LEFT OUTER JOIN product.product p ON s.psv_product = p.product_id " +
            "WHERE (?1 IS NULL OR p.product_id = ?1) " +
            "AND (?2 IS NULL OR s.style_id = ?2 ) " +
            "GROUP BY s.style_id", nativeQuery = true)
    List<ProductStyleVariant> findStyle(String productId, String styleId);


    @Modifying
    @Query(value = "DELETE FROM product.size_details sd WHERE sd.size_id = ?1",nativeQuery = true)
    void deleteSize(String skuId);


    @Query(value = "SELECT DISTINCT psv.* " +
            "FROM product.product_style_variant psv " +
            "INNER JOIN product.product p ON psv.psv_product = p.product_id "+
            "INNER JOIN product.size_details sd ON psv.style_id = sd.psv_id "+
            "WHERE psv.searchable_text @@ plainto_tsquery('english', ?1) " +
            "AND (p.product_master_category = ANY(CAST(?2 AS text[])) OR CAST(?2 AS text[]) IS NULL)" +
            "AND (p.product_category = ANY(CAST(?3 AS text[])) OR CAST(?3 AS text[]) IS NULL) " +
            "AND (p.product_sub_category = ANY(CAST(?4 AS text[])) OR CAST(?4 AS text[]) IS NULL) " +
            "AND (p.product_brand = ANY(CAST(?5 AS text[])) OR CAST(?5 AS text[]) IS NULL) " +
            "AND (p.gender = ANY(CAST(?6 AS text[])) OR CAST(?6 AS text[]) IS NULL) " +
            "AND (psv.colour = ANY(CAST(?7 AS text[])) OR CAST(?7 AS text[]) IS NULL) " +
            "AND (sd.size = ANY(CAST(?8 AS text[])) OR CAST(?8 AS text[]) IS NULL) " +
            "AND (psv.discount_percentage >= CAST(?9 AS numeric) OR CAST(?9 AS numeric) IS NULL) " +
            "AND (psv.final_price >= CAST(?10 AS numeric) OR CAST(?10 AS numeric) IS NULL) " +
            "AND (psv.final_price <= CAST(?11 AS numeric) OR CAST(?11 AS numeric) IS NULL)",nativeQuery = true)
    Page<ProductStyleVariant> findProductBySearchString(String searchInput, String[] masterCategoryName, String[] categoryName,
                                                        String[] subCategoryName, String[] brandName, String[] gender, String[] colour,
                                                        String[] size, Integer discountPercentage, Integer minPrice, Integer maxPrice,
                                                        PageRequest pageRequest);

    @Query(value = "WITH search_results AS (" +
            "SELECT psv.* " +
            "FROM product.product_style_variant psv " +
            "INNER JOIN product.product p ON psv.psv_product = p.product_id " +
            "LEFT JOIN product.size_details sd ON psv.style_id = sd.psv_id " +
            "WHERE psv.searchable_text @@ plainto_tsquery('english', ?1) ) " +
//            "AND (p.product_master_category = ANY(CAST(?2 AS text[])) OR CAST(?2 AS text[]) IS NULL) " +
//            "AND (p.product_category = ANY(CAST(?3 AS text[])) OR CAST(?3 AS text[]) IS NULL) " +
//            "AND (p.product_sub_category = ANY(CAST(?4 AS text[])) OR CAST(?4 AS text[]) IS NULL) " +
//            "AND (p.product_brand = ANY(CAST(?5 AS text[])) OR CAST(?5 AS text[]) IS NULL) " +
//            "AND (p.gender = ANY(CAST(?6 AS text[])) OR CAST(?6 AS text[]) IS NULL) " +
//            "AND (psv.colour = ANY(CAST(?7 AS text[])) OR CAST(?7 AS text[]) IS NULL) " +
//            "AND (sd.size = ANY(CAST(?8 AS text[])) OR CAST(?8 AS text[]) IS NULL) " +
//            "AND (psv.discount_percentage >= CAST(?9 AS numeric) OR CAST(?9 AS numeric) IS NULL) " +
//            "AND (psv.final_price >= CAST(?10 AS numeric) OR CAST(?10 AS numeric) IS NULL) " +
//            "AND (psv.final_price <= CAST(?11 AS numeric) OR CAST(?11 AS numeric) IS NULL)) " +
            "SELECT " +
            "array_agg(DISTINCT master_category) AS masterCategories, " +
            "array_agg(DISTINCT category) AS categories, " +
            "array_agg(DISTINCT sub_category) AS subCategories, " +
            "array_agg(DISTINCT brand) AS brands, " +
            "array_agg(DISTINCT gender) AS gender, " +
            "json_agg(json_build_object('colour', colour, 'hexCode', colour_hex_code)) AS colours,"+
            "array_agg(DISTINCT size) AS sizes, " +
            "json_agg(json_build_object('discountPercentage', discount_percentage, 'discountPercentageText', discount_percentage_text)) AS discount, " +
            "MIN(final_price) AS minPrice, " +
            "MAX(final_price) AS maxPrice " +
            "FROM ( " +
            "SELECT DISTINCT " +
            "p.product_master_category AS master_category, " +
            "p.product_category AS category, " +
            "p.product_sub_category AS sub_category, " +
            "p.product_brand AS brand, " +
            "p.gender, " +
            "sr.colour , " +
            "sr.colour_hex_code, " +
            "sd.size, " +
            "sr.discount_percentage, " +
            "sr.discount_percentage_text, " +
            "sr.final_price " +
            "FROM search_results sr " +
            "INNER JOIN product.product p ON sr.psv_product = p.product_id " +
            "LEFT JOIN product.size_details sd ON sr.style_id = sd.psv_id)",
            nativeQuery = true)
    Map<String, Object> findFilters(String searchInput, String[] masterCategoryName, String[] categoryName,
                                    String[] subCategoryName, String[] brandName, String[] gender, String[] colour,
                                    String[] size, Integer discountPercentage, Integer minPrice, Integer maxPrice);


    //------------------------------------------------------------------------------------------------------------------------
    @Query(value = "SELECT DISTINCT psv.* FROM product.product_style_variant psv " +
            "INNER JOIN product.product p ON psv.psv_product = p.product_id " +
            "INNER JOIN product.size_details sd ON psv.style_id = sd.psv_id "+
            "WHERE (p.product_master_category = ?1 OR ?1 IS NULL) "+
            "AND (p.product_category = ?2 OR ?2 IS NULL) " +
            "AND (p.product_sub_category = ?3 OR ?3 IS NULL) " +
            "AND (p.product_brand = ?4 OR ?4 IS NULL) " +
            "AND (p.gender = ?5 OR ?5 IS NULL) " +
            "AND (psv.colour = ?6 OR ?6 IS NULL) " +
            "AND (sd.size = ?7 OR ?7 IS NULL) " +
            "AND (psv.discount_percentage >= ?8 OR ?8 IS NULL) " +
            "AND (psv.final_price >= ?9 OR ?9 IS NULL) " +
            "AND (psv.final_price <= ?10 OR ?10 IS NULL) ",nativeQuery = true)
    Page<ProductStyleVariant> findProductByParameters(String masterCategoryName, String categoryName, String subCategoryName,
                                                      String brandName, String gender, String colour, String size, Integer discountPercentage,
                                                      Integer minPrice, Integer maxPrice, PageRequest pageRequest);

}
