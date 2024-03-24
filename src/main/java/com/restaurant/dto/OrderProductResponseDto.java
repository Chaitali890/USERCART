package com.restaurant.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderProductResponseDto {

	private List<ProductDto> productDtoList;
	
	private Long orderAmount;
}
