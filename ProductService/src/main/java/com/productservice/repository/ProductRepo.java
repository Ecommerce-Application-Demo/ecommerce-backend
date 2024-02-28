package com.productservice.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.Product;

public interface ProductRepo extends CrudRepository<Product, UUID> {

}
