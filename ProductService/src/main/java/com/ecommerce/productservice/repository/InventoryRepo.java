package com.ecommerce.productservice.repository;

import com.ecommerce.productservice.entity.warehousemanagement.Inventory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface InventoryRepo extends ListCrudRepository<Inventory, String> {

    List<Inventory> findBySizeVariantId(String sizeVariantId);

    @Modifying
    @Query(value = "UPDATE product.size_details " +
            "SET quantity = (" +
            "  SELECT SUM(quantity) " +
            "  FROM product.inventory i" +
            "  WHERE i.size_variant_id = ?1 " +
            ") " +
            "WHERE size_id = ?1",nativeQuery = true)
    void updateQuantity(String sizeid);

     @Query(value = "SELECT SUM(quantity) " +
                    "FROM product.inventory i " +
                    "WHERE i.size_variant_id = ?1",nativeQuery = true)
    Integer SumQuantityBySizeVariantId(String sizeVariantId);

     @Modifying
     void deleteBySizeVariantId(String sizeVariantId);
}
