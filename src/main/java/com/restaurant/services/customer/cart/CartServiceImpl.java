package com.restaurant.services.customer.cart;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.restaurant.dto.AddProductToCartDto;
import com.restaurant.dto.CartItemDto;
import com.restaurant.dto.OrderDto;
import com.restaurant.dto.PlaceOrderDto;
import com.restaurant.entity.CartItems;
import com.restaurant.entity.Coupon;
import com.restaurant.entity.Order;
import com.restaurant.entity.Product;
import com.restaurant.entity.User;
import com.restaurant.enums.OrderStatus;
import com.restaurant.exceptions.ValidationException;
import com.restaurant.repository.CartItemRepository;
import com.restaurant.repository.CouponRepository;
import com.restaurant.repository.OrderRepository;
import com.restaurant.repository.ProductRepository;
import com.restaurant.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CouponRepository couponRepository;

	public ResponseEntity<?> addproductToCart(AddProductToCartDto addProductToCartDto){
		Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductToCartDto.getUserId(), OrderStatus.Pending);
		Optional<CartItems> optionalCartItems = cartItemRepository.findByProductIdAndOrderIdAndUserId(addProductToCartDto.getProductId(),activeOrder.getId(),addProductToCartDto.getUserId());
		
		if(optionalCartItems.isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}else
		{
			Optional<Product> optionalProduct = productRepository.findById(addProductToCartDto.getProductId());
			Optional<User> optionalUser = userRepository.findById(addProductToCartDto.getUserId());
			
			if(optionalProduct.isPresent() && optionalUser.isPresent()) {
				CartItems cart = new CartItems();
				cart.setProduct(optionalProduct.get());
				cart.setPrice(optionalProduct.get().getPrice());
				cart.setQuantity(1L);
				cart.setUser(optionalUser.get());
				cart.setOrder(activeOrder);
				
				CartItems updatedCart = cartItemRepository.save(cart);
				
				activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cart.getPrice());
				activeOrder.setAmount(activeOrder.getAmount() + cart.getPrice());
				activeOrder.getCartItems().add(cart);
				orderRepository.save(activeOrder);
				return ResponseEntity.status(HttpStatus.CREATED).body(cart);
			}
			else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user or product not found");
			}
		}		
	}
	
	public OrderDto getCardByUserId(Long userId) {
		Order activateOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
		List<CartItemDto> cartItemDtoList = activateOrder.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList());
		OrderDto orderDto = new OrderDto();
		orderDto.setAmount(activateOrder.getAmount());
		orderDto.setId(activateOrder.getId());
		orderDto.setOrderStatus(activateOrder.getOrderStatus());
		orderDto.setDiscount(activateOrder.getDiscount());
		orderDto.setTotalAmount(activateOrder.getTotalAmount());
		orderDto.setCartItems(cartItemDtoList);
		
		if(activateOrder.getCoupon()!=null) {
			orderDto.setCouponName(activateOrder.getCoupon().getName());
		}
		
		return orderDto;
	}
		
		
	public OrderDto applyCoupon(Long userId, String code) {
		Order activateOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
		Coupon coupon = couponRepository.findByCode(code).orElseThrow(()->new ValidationException("coupon not found"));
		
		if(couponIsExpired(coupon)) {
			throw new ValidationException("coupon not expired");
		}
		
		double discountAmount = ((coupon.getDiscount()/100.0) * activateOrder.getTotalAmount());
		double netAmount = activateOrder.getTotalAmount() - discountAmount;
		
		activateOrder.setAmount((long)netAmount);
		activateOrder.setDiscount((long)discountAmount);
		activateOrder.setCoupon(coupon);
		
		orderRepository.save(activateOrder);
		return activateOrder.getOrderDto();
		
	}
	
	private boolean couponIsExpired(Coupon coupon) {
		Date currentDate = new Date();
		Date expirationDate = coupon.getExpirationDate();
		
		return expirationDate !=null && currentDate.after(expirationDate);
	}
	
	
	public OrderDto increaseProductQuantity(AddProductToCartDto  addProductToCartDto) {
		Order activateOrder = orderRepository.findByUserIdAndOrderStatus(addProductToCartDto.getUserId(), OrderStatus.Pending);
		Optional<Product> optionalProduct = productRepository.findById(addProductToCartDto.getProductId());
		
		Optional<CartItems> optionalCartItems = cartItemRepository.findByProductIdAndOrderIdAndUserId(addProductToCartDto.getProductId(), activateOrder.getId(), addProductToCartDto.getUserId());
		
		if(optionalProduct.isPresent() && optionalCartItems.isPresent()) {
			
			CartItems cartItem = optionalCartItems.get();
			Product product = optionalProduct.get();
			
			activateOrder.setAmount(activateOrder.getAmount() + product.getPrice());
			activateOrder.setTotalAmount(activateOrder.getTotalAmount() + product.getPrice());
			
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			
			if(activateOrder.getCoupon()!=null) {
				double discountAmount = ((activateOrder.getCoupon().getDiscount()/100.0) * activateOrder.getTotalAmount());
				double netAmount = activateOrder.getTotalAmount() - discountAmount;
				
				activateOrder.setAmount((long)netAmount);
				activateOrder.setDiscount((long)discountAmount);
			}
			
			cartItemRepository.save(cartItem);
			orderRepository.save(activateOrder);
			return activateOrder.getOrderDto();
		}
			return null;
	}
	
	
	public OrderDto decreaseProductQuantity(AddProductToCartDto  addProductToCartDto) {
		Order activateOrder = orderRepository.findByUserIdAndOrderStatus(addProductToCartDto.getUserId(), OrderStatus.Pending);
		Optional<Product> optionalProduct = productRepository.findById(addProductToCartDto.getProductId());
		
		Optional<CartItems> optionalCartItems = cartItemRepository.findByProductIdAndOrderIdAndUserId(addProductToCartDto.getProductId(), activateOrder.getId(), addProductToCartDto.getUserId());
		
		if(optionalProduct.isPresent() && optionalCartItems.isPresent()) {
			
			CartItems cartItem = optionalCartItems.get();
			Product product = optionalProduct.get();
			
			activateOrder.setAmount(activateOrder.getAmount() - product.getPrice());
			activateOrder.setTotalAmount(activateOrder.getTotalAmount() - product.getPrice());
			
			cartItem.setQuantity(cartItem.getQuantity() - 1);
			
			if(activateOrder.getCoupon()!=null) {
				double discountAmount = ((activateOrder.getCoupon().getDiscount()/100.0) * activateOrder.getTotalAmount());
				double netAmount = activateOrder.getTotalAmount() - discountAmount;
				
				activateOrder.setAmount((long)netAmount);
				activateOrder.setDiscount((long)discountAmount);
			}
			
			cartItemRepository.save(cartItem);
			orderRepository.save(activateOrder);
			return activateOrder.getOrderDto();
		}
			return null;
	}
	
	public OrderDto placeOrder(PlaceOrderDto placeOrderDto) {
		Order activateOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.Pending);
		Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());
		if(optionalUser.isPresent()) {
			activateOrder.setOrderDescription(placeOrderDto.getOrderDescription());
			activateOrder.setAddress(placeOrderDto.getAddress());
			activateOrder.setDate(new Date());
			activateOrder.setOrderStatus(OrderStatus.Placed);
			activateOrder.setTrackingId(UUID.randomUUID());
			
			orderRepository.save(activateOrder);
		}
		
		return null;
	}	
	
	public List<OrderDto> getMyPlacedOrder(Long userId){
		return orderRepository.findByUserIdAndOrderStatusIn(userId, List.of(OrderStatus.Placed, OrderStatus.Shipped, OrderStatus.Delivered)).stream().map(Order::getOrderDto).collect(Collectors.toList());
		
	}
	
	
	public OrderDto searchOrderbyTrackingId(UUID trackingId) {
		Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);
			if(optionalOrder.isPresent()) {
				return optionalOrder.get().getOrderDto();
			}
			return null;
	}
	
}

