package com.productservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.Sku;

public interface SkuRepo extends CrudRepository<Sku, String> {

}
