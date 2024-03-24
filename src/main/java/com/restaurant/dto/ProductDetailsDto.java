package com.restaurant.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductDetailsDto {

	private ProductDto productDto;
	
	private List<ReviewDto> reviewDto;
	
	private List<FAQDto> faqDtoList;
	
}
