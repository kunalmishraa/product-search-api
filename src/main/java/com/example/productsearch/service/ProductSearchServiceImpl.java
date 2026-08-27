package com.example.productsearch.service;

import com.example.productsearch.dto.ProductResponse;
import com.example.productsearch.model.ProductCategory;
import com.example.productsearch.util.ProductMapper;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service("productSearchApplicationService")
public class ProductSearchServiceImpl implements ProductSearchService {
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 20;

	private final com.example.productsearch.elasticsearch.ProductSearchService elasticsearchService;
	private final ProductMapper productMapper;

	public ProductSearchServiceImpl(
			com.example.productsearch.elasticsearch.ProductSearchService elasticsearchService,
			ProductMapper productMapper) {
		this.elasticsearchService = elasticsearchService;
		this.productMapper = productMapper;
	}

	@Override
	public List<ProductResponse> searchProducts(
			String q,
			String category,
			String brand,
			BigDecimal minPrice,
			BigDecimal maxPrice,
			Integer page,
			Integer size,
			String sort) {
		ProductCategory productCategory = category == null || category.isBlank()
				? null
				: ProductCategory.valueOf(category.toUpperCase());

		return elasticsearchService.search(
						q,
						productCategory,
						brand,
						minPrice,
						maxPrice,
						page == null || page < 0 ? DEFAULT_PAGE : page,
						size == null || size <= 0 ? DEFAULT_SIZE : size,
						sort)
				.stream()
				.map(productMapper::toProductResponse)
				.toList();
	}
}
