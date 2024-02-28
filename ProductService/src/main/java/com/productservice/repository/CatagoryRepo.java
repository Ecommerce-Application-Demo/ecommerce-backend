package com.productservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.Catagory;

public interface CatagoryRepo extends CrudRepository<Catagory, String> {

}
