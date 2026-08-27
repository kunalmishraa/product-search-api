package com.example.productsearch.redis;

import com.example.productsearch.dto.ProductResponse;
import java.util.List;

public interface ProductCacheService {
	List<ProductResponse> getPopularProducts();

	void cachePopularProducts(List<ProductResponse> products);
}
