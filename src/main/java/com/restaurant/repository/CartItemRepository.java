package com.restaurant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.restaurant.entity.CartItems;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems,Long> {
//
//	Optional<CartItems> findByProductIdAndOrderIdAndUserId(Long productId, Long userId, Long orderId);

	Optional<CartItems> findByProductIdAndOrderIdAndUserId(Long productId, Long orderId, Long userId);

	
	
}
