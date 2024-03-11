package com.productservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.Category;

public interface CategoryRepo extends CrudRepository<Category, String> {

}
