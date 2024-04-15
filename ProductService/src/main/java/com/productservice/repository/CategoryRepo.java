package com.productservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.Category;

import java.util.List;

public interface CategoryRepo extends CrudRepository<Category, String> {

    @Query(value = "SELECT * FROM product.category c LEFT OUTER JOIN product.master_category mc " +
                    "ON c.category_master_category = mc.master_category_name " +
                    "WHERE (?1 IS NULL OR c.category_name = ?1) " +
                    "AND (?2 IS NULL OR c.category_id = CAST(?2 AS UUID)) " +
                    "AND (?3 IS NULL OR mc.master_category_name = ?3)", nativeQuery = true)
    List<Category> findCategory(String categoryName, String categoryId, String masterCategory);

}
