package com.restaurant.services.customer.cart;


import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import com.restaurant.dto.AddProductToCartDto;
import com.restaurant.dto.OrderDto;
import com.restaurant.dto.PlaceOrderDto;

public interface CartService {

	ResponseEntity<?> addproductToCart(AddProductToCartDto addProductToCartDto);
		OrderDto getCardByUserId(Long userId);
		OrderDto applyCoupon(Long userId, String code);
		OrderDto increaseProductQuantity(AddProductToCartDto  addProductToCartDto);
		OrderDto decreaseProductQuantity(AddProductToCartDto addProductToCartDto);
		OrderDto placeOrder(PlaceOrderDto placeOrderDto);
		List<OrderDto> getMyPlacedOrder(Long userId);
		OrderDto searchOrderbyTrackingId(UUID trackingId);
			
}
