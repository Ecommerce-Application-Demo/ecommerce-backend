package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.warehousemanagement.Warehouse;
import org.springframework.data.repository.ListCrudRepository;

public interface WarehouseRepo extends ListCrudRepository<Warehouse, Integer> {
}
