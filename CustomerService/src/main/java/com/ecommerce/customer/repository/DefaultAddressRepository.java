package com.ecommerce.customer.repository;

import org.springframework.data.repository.CrudRepository;
import com.ecommerce.customer.entity.DefaultAddress;

public interface DefaultAddressRepository extends CrudRepository<DefaultAddress, String> {

}
