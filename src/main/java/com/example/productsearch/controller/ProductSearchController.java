package com.example.productsearch.controller;

import com.example.productsearch.dto.ProductResponse;
import com.example.productsearch.service.ProductSearchService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/search")
public class ProductSearchController {
	private final ProductSearchService productSearchService;

	public ProductSearchController(ProductSearchService productSearchService) {
		this.productSearchService = productSearchService;
	}

	@GetMapping
	public List<ProductResponse> searchProducts(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) BigDecimal minPrice,
			@RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String sort) {
		return productSearchService.searchProducts(q, category, brand, minPrice, maxPrice, page, size, sort);
	}
}
