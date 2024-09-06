package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

public interface CartRepository extends ListCrudRepository<Cart,String> {
     @Query(value = "SELECT * FROM order_service.cart c WHERE c.browser_session_id = ?1", nativeQuery = true)
     Cart findByBrowserSessionId(String browserSessionId);

     @Query(value = "SELECT * FROM order_service.cart c WHERE c.user_id = ?1", nativeQuery = true)
     Cart findByUserId(String userId);
}
