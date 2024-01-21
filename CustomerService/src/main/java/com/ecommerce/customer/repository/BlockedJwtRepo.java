package com.ecommerce.customer.repository;

import org.springframework.data.repository.CrudRepository;
import com.ecommerce.customer.entity.BlockedJwt;

public interface BlockedJwtRepo extends CrudRepository<BlockedJwt, String> {

}
