package com.ecommerce.productservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ecommerce.productservice.entity.MasterCategory;

import java.util.List;

public interface MasterCategoryRepo extends CrudRepository<MasterCategory, String> {

    @Query(value = "SELECT * " +
                    "FROM product.master_category " +
                    "WHERE (?1 IS NULL OR master_category_name = ?1 ) " +
                    "AND (?2 IS NULL OR master_category_id = ?2) ", nativeQuery = true)
    List<MasterCategory> findMasterCategory(String masterCategoryName,String masterCategoryId);
}
