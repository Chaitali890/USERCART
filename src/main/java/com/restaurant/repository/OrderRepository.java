package com.restaurant.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.restaurant.entity.Order;
import com.restaurant.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {


	Order findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
	
	List<Order> findAllByOrderStatusIn(List<OrderStatus> orderStatusList);

	List<Order> findByUserIdAndOrderStatusIn(Long userId, List<OrderStatus> orderStatus);

	Optional<Order> findByTrackingId(UUID trackingId);

	List<Order> findByDateBetweenAndStatus(Date startOfMonth, Date endOfMonth, OrderStatus delivered);

	Long countByOrderStatus(OrderStatus status);
	
}
