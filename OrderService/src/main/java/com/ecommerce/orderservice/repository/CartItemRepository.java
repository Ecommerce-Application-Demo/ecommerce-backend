package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CartItemRepository extends ListCrudRepository<CartItem,String> {

    @Query(value = "SELECT c.sku_id FROM order_service.cart_item c WHERE c.cart_id = ?1", nativeQuery = true)
    List<String> findSkuIds(String cartId);

    @Query(value = "SELECT c.* FROM order_service.cart_item c WHERE c.cart_id = ?1 AND c.sku_id = ?2", nativeQuery = true)
    CartItem findByCartIdAndSkuId(String cartId, String skuId);

    @Query(value = "SELECT * FROM order_service.cart_item c WHERE c.cart_id = ?1", nativeQuery = true)
    List<CartItem> findByCartId(String cartId);

    @Query(value = "SELECT COUNT(*) FROM order_service.cart_item c WHERE c.cart_id = ?1", nativeQuery = true)
    Integer countItemsByCartId(String cartId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM order_service.cart_item c WHERE c.cart_id = ?1 " +
                    "AND c.sku_id = ANY(CAST(?2 AS text[])) OR CAST(?2 AS text[]) IS NULL", nativeQuery = true)
    void deleteSkuByCartId(String cartId, String[] skuIds);
}
