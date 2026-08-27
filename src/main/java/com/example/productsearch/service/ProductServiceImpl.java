package com.example.productsearch.service;

import com.example.productsearch.dto.ProductRequest;
import com.example.productsearch.dto.ProductResponse;
import com.example.productsearch.exception.ProductNotFoundException;
import com.example.productsearch.model.Product;
import com.example.productsearch.model.ProductCategory;
import com.example.productsearch.redis.ProductCacheService;
import com.example.productsearch.repository.ProductRepository;
import com.example.productsearch.util.ProductMapper;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 20;

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final ProductCacheService productCacheService;
	private final com.example.productsearch.elasticsearch.ProductSearchService elasticsearchService;

	public ProductServiceImpl(
			ProductRepository productRepository,
			ProductMapper productMapper,
			ProductCacheService productCacheService,
			com.example.productsearch.elasticsearch.ProductSearchService elasticsearchService) {
		this.productRepository = productRepository;
		this.productMapper = productMapper;
		this.productCacheService = productCacheService;
		this.elasticsearchService = elasticsearchService;
	}

	@Override
	public ProductResponse createProduct(ProductRequest request) {
		Product product = productMapper.toProduct(request);
		Instant now = Instant.now();
		product.setCreatedAt(now);
		product.setUpdatedAt(now);

		Product savedProduct = productRepository.save(product);
		elasticsearchService.indexProduct(productMapper.toProductSearchDocument(savedProduct));
		return productMapper.toProductResponse(savedProduct);
	}

	@Override
	public List<ProductResponse> getAllProducts(Integer page, Integer size, String sort) {
		return productRepository.findAll(pageable(page, size, sort)).stream()
				.map(productMapper::toProductResponse)
				.toList();
	}

	@Override
	public ProductResponse getProductById(String productId) {
		return productRepository.findById(productId)
				.map(productMapper::toProductResponse)
				.orElseThrow(() -> new ProductNotFoundException(productId));
	}

	@Override
	public List<ProductResponse> getProductsByCategory(String category, Integer page, Integer size, String sort) {
		ProductCategory productCategory = ProductCategory.valueOf(category.toUpperCase());
		return productRepository.findByCategory(productCategory, pageable(page, size, sort)).stream()
				.map(productMapper::toProductResponse)
				.toList();
	}

	@Override
	public List<ProductResponse> getProductsByBrand(String brand, Integer page, Integer size, String sort) {
		return productRepository.findByBrandIgnoreCase(brand, pageable(page, size, sort)).stream()
				.map(productMapper::toProductResponse)
				.toList();
	}

	@Override
	public List<ProductResponse> getPopularProducts() {
		List<ProductResponse> cachedProducts = productCacheService.getPopularProducts();
		if (!cachedProducts.isEmpty()) {
			return cachedProducts;
		}

		List<ProductResponse> products = productRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))).stream()
				.map(productMapper::toProductResponse)
				.toList();
		productCacheService.cachePopularProducts(products);
		return products;
	}

	private Pageable pageable(Integer page, Integer size, String sort) {
		int pageNumber = page == null || page < 0 ? DEFAULT_PAGE : page;
		int pageSize = size == null || size <= 0 ? DEFAULT_SIZE : size;
		return PageRequest.of(pageNumber, pageSize, parseSort(sort));
	}

	private Sort parseSort(String sort) {
		if (sort == null || sort.isBlank()) {
			return Sort.unsorted();
		}
		String[] parts = sort.split(",");
		Sort.Direction direction = parts.length > 1 && "desc".equalsIgnoreCase(parts[1])
				? Sort.Direction.DESC
				: Sort.Direction.ASC;
		return Sort.by(direction, parts[0]);
	}
}
