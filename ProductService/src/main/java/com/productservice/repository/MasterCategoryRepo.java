package com.productservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.MasterCategory;

public interface MasterCategoryRepo extends CrudRepository<MasterCategory, String> {

}
