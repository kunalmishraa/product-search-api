package com.example.productsearch.elasticsearch;

import com.example.productsearch.model.ProductCategory;
import java.math.BigDecimal;
import java.util.List;

public interface ProductSearchService {
	void indexProduct(ProductSearchDocument document);

	List<ProductSearchDocument> search(
			String q,
			ProductCategory category,
			String brand,
			BigDecimal minPrice,
			BigDecimal maxPrice,
			int page,
			int size,
			String sort);
}
