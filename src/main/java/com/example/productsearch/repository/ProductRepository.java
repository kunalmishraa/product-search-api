package com.example.productsearch.repository;

import com.example.productsearch.model.Product;
import com.example.productsearch.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
	Page<Product> findByCategory(ProductCategory category, Pageable pageable);

	Page<Product> findByBrandIgnoreCase(String brand, Pageable pageable);
}
