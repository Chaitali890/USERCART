package com.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurant.entity.WishList;

public interface WishListRepository extends JpaRepository<WishList,Long> {

	List<WishList> findByUserId(Long userId);
}
