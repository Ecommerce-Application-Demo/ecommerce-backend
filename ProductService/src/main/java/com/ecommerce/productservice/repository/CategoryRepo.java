package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CategoryRepo extends ListCrudRepository<Category, String> {

    @Query(value = "SELECT * FROM product.category c LEFT OUTER JOIN product.master_category mc " +
                    "ON c.category_master_category = mc.master_category_name " +
                    "WHERE (?1 IS NULL OR c.category_name = ?1) " +
                    "AND (?2 IS NULL OR c.category_id = ?2) " +
                    "AND (?3 IS NULL OR mc.master_category_name = ?3)", nativeQuery = true)
    List<Category> findCategory(String categoryName, String categoryId, String masterCategory);

}
