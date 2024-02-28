package com.productservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.productservice.entity.MasterCatagory;

public interface MasterCatagoryRepo extends CrudRepository<MasterCatagory, String> {

}
