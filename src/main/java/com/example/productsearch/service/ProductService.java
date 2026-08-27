package com.example.productsearch.service;

import com.example.productsearch.dto.ProductRequest;
import com.example.productsearch.dto.ProductResponse;
import java.util.List;

public interface ProductService {
	ProductResponse createProduct(ProductRequest request);

	List<ProductResponse> getAllProducts(Integer page, Integer size, String sort);

	ProductResponse getProductById(String productId);

	List<ProductResponse> getProductsByCategory(String category, Integer page, Integer size, String sort);

	List<ProductResponse> getProductsByBrand(String brand, Integer page, Integer size, String sort);

	List<ProductResponse> getPopularProducts();
}
