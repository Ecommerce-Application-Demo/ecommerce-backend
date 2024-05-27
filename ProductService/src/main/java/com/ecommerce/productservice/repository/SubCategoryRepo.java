package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.SubCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface SubCategoryRepo extends ListCrudRepository<SubCategory, String> {

    @Query(value = "SELECT * FROM product.sub_category sc LEFT OUTER JOIN product.category c " +
            "ON sc.sub_category_category = c.category_name " +
            "WHERE (?1 IS NULL OR sc.sub_category_name = ?1) " +
            "AND (?2 IS NULL OR sc.sub_category_id = ?2) " +
            "AND (?3 IS NULL OR c.category_name = ?3)", nativeQuery = true)
    List<SubCategory> findSubCategory(String subCategoryName, String subCategoryId, String categoryName);
}
