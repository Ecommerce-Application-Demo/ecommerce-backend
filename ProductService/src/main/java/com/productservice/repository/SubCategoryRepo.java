package com.productservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.SubCategory;

public interface SubCategoryRepo extends CrudRepository<SubCategory, String> {

}
