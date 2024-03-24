package com.restaurant.controller.customer;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.dto.AddProductToCartDto;
import com.restaurant.dto.OrderDto;
import com.restaurant.dto.PlaceOrderDto;
import com.restaurant.exceptions.ValidationException;
import com.restaurant.services.customer.cart.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;
	
	@PostMapping("/cart")
	public ResponseEntity<?> addProductToCart(@RequestBody AddProductToCartDto addProductinCartDto){
		return cartService.addproductToCart(addProductinCartDto);
	}
	
	@GetMapping("/cart/{userId}")
	public ResponseEntity<?> getCardByUserId(@PathVariable Long userId){
		OrderDto orderDto = cartService.getCardByUserId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(orderDto);
	}
	
	@GetMapping("/coupon/{userId}/{code}")
	public ResponseEntity<?> applyCoupon(@PathVariable Long userId, @PathVariable String code){
		try
		{
			OrderDto orderDto = cartService.applyCoupon(userId, code);
				return ResponseEntity.ok(orderDto);
		}catch(ValidationException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		}
		
	}
	
	@PostMapping("/addition")
	public ResponseEntity<OrderDto> increaseProductQuantity(@RequestBody AddProductToCartDto addProductToCartDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(cartService.increaseProductQuantity(addProductToCartDto));
	}

	@PostMapping("/deduction")
	public ResponseEntity<OrderDto> decreaseProductQuantity(@RequestBody AddProductToCartDto addProductToCartDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(cartService.decreaseProductQuantity(addProductToCartDto));
	}
	
	@PostMapping("/placeOrder")
	public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderDto placeOrderDto){
			return ResponseEntity.status(HttpStatus.CREATED).body(cartService.placeOrder(placeOrderDto));
	}
	
	@GetMapping("/myOrders/{userId}")
	public ResponseEntity<List<OrderDto>> getMyPlacedOrders(@PathVariable Long userId){
		return ResponseEntity.ok(cartService.getMyPlacedOrder(userId));
	}
	
	
	
}
