package com.example.productsearch.service;

import com.example.productsearch.dto.ProductResponse;
import java.math.BigDecimal;
import java.util.List;

public interface ProductSearchService {
	List<ProductResponse> searchProducts(
			String q,
			String category,
			String brand,
			BigDecimal minPrice,
			BigDecimal maxPrice,
			Integer page,
			Integer size,
			String sort);
}
