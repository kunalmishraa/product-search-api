package com.example.productsearch.service;

import com.example.productsearch.dto.InventoryRequest;
import com.example.productsearch.dto.InventoryResponse;
import com.example.productsearch.exception.InventoryNotFoundException;
import com.example.productsearch.exception.ProductNotFoundException;
import com.example.productsearch.model.Inventory;
import com.example.productsearch.repository.InventoryRepository;
import com.example.productsearch.repository.ProductRepository;
import com.example.productsearch.util.ProductMapper;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 20;

	private final InventoryRepository inventoryRepository;
	private final ProductRepository productRepository;
	private final ProductMapper productMapper;

	public InventoryServiceImpl(
			InventoryRepository inventoryRepository,
			ProductRepository productRepository,
			ProductMapper productMapper) {
		this.inventoryRepository = inventoryRepository;
		this.productRepository = productRepository;
		this.productMapper = productMapper;
	}

	@Override
	public InventoryResponse createInventory(String productId, InventoryRequest request) {
		if (!productRepository.existsById(productId)) {
			throw new ProductNotFoundException(productId);
		}

		Inventory inventory = productMapper.toInventory(productId, request);
		inventory.setUpdatedAt(Instant.now());
		return productMapper.toInventoryResponse(inventoryRepository.save(inventory));
	}

	@Override
	public InventoryResponse getProductInventory(String productId) {
		return inventoryRepository.findByProductId(productId)
				.map(productMapper::toInventoryResponse)
				.orElseThrow(() -> new InventoryNotFoundException(productId));
	}

	@Override
	public List<InventoryResponse> getAllInventory(Integer page, Integer size) {
		int pageNumber = page == null || page < 0 ? DEFAULT_PAGE : page;
		int pageSize = size == null || size <= 0 ? DEFAULT_SIZE : size;
		return inventoryRepository.findAll(PageRequest.of(pageNumber, pageSize)).stream()
				.map(productMapper::toInventoryResponse)
				.toList();
	}
}
