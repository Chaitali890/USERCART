package com.restaurant.services.customer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.restaurant.dto.ProductDetailsDto;
import com.restaurant.dto.ProductDto;
import com.restaurant.entity.FAQ;
import com.restaurant.entity.Product;
import com.restaurant.entity.Review;
import com.restaurant.repository.FAQRepository;
import com.restaurant.repository.ProductRepository;
import com.restaurant.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService  {
	
	private final ProductRepository productRepository;
	
	private final FAQRepository faqRepository;
	
	private final ReviewRepository reviewRepository;

	public List<ProductDto> getAllProducts(){
		List<Product> products = productRepository.findAll();
		return products.stream().map(Product::getDto).collect(Collectors.toList());
	}
	
	public List<ProductDto> searchProductByTitle(String name){
		List<Product> products = productRepository.findAllByNameContaining(name);
		return products.stream().map(Product::getDto).collect(Collectors.toList());
	}

	public ProductDetailsDto getProductDetailById(Long productId) {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		
		if(optionalProduct.isPresent()) {
			List<FAQ> faqList = faqRepository.findAllByProductId(productId);
			List<Review> reviewList = reviewRepository.findAllByProductId(productId);
			ProductDetailsDto productDetailsDto = new ProductDetailsDto();
			productDetailsDto.setProductDto(optionalProduct.get().getDto());
			productDetailsDto.setFaqDtoList(faqList.stream().map(FAQ::getFAQDto).collect(Collectors.toList()));
			productDetailsDto.setReviewDto(reviewList.stream().map(Review::getDto).collect(Collectors.toList()));
			return productDetailsDto;
		}
		
		return null;
	}
}
