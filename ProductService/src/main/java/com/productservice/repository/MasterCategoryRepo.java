package com.productservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.MasterCategory;

import java.util.List;
import java.util.UUID;

public interface MasterCategoryRepo extends CrudRepository<MasterCategory, String> {

    @Query(value = "SELECT * " +
                    "FROM product.master_category " +
                    "WHERE (?1 IS NULL OR master_category_name = ?1 ) " +
                    "AND (?2 IS NULL OR master_category_id = CAST(?2 AS UUID)) ", nativeQuery = true)
    List<MasterCategory> findMasterCategory(String masterCategoryName,String masterCategoryId);
}
