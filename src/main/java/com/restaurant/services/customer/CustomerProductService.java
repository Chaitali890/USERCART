package com.restaurant.services.customer;

import java.util.List;

import com.restaurant.dto.ProductDetailsDto;
import com.restaurant.dto.ProductDto;

public interface CustomerProductService {

	List<ProductDto> getAllProducts();
	
	List<ProductDto> searchProductByTitle(String title);

	ProductDetailsDto getProductDetailById(Long productId);
}
