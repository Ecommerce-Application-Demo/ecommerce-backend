package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.PincodeDetails;
import org.springframework.data.repository.ListCrudRepository;

public interface PincodeRepo extends ListCrudRepository<PincodeDetails, String> {
}
