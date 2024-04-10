package com.productservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.Brand;

public interface BrandRepo extends CrudRepository<Brand, String> {

}
