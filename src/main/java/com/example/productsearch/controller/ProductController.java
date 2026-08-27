package com.example.productsearch.controller;

import com.example.productsearch.dto.ProductRequest;
import com.example.productsearch.dto.ProductResponse;
import com.example.productsearch.service.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	public ProductResponse createProduct(@RequestBody ProductRequest request) {
		return productService.createProduct(request);
	}

	@GetMapping
	public List<ProductResponse> getAllProducts(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String sort) {
		return productService.getAllProducts(page, size, sort);
	}

	@GetMapping("/{productId}")
	public ProductResponse getProductById(@PathVariable String productId) {
		return productService.getProductById(productId);
	}

	@GetMapping("/category/{category}")
	public List<ProductResponse> getProductsByCategory(
			@PathVariable String category,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String sort) {
		return productService.getProductsByCategory(category, page, size, sort);
	}

	@GetMapping("/brand/{brand}")
	public List<ProductResponse> getProductsByBrand(
			@PathVariable String brand,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) String sort) {
		return productService.getProductsByBrand(brand, page, size, sort);
	}

	@GetMapping("/popular")
	public List<ProductResponse> getPopularProducts() {
		return productService.getPopularProducts();
	}
}
