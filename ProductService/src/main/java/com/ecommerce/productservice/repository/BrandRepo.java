package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.Brand;
import org.springframework.data.repository.ListCrudRepository;

public interface BrandRepo extends ListCrudRepository<Brand, String> {

}
