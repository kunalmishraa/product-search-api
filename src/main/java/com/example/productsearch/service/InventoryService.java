package com.example.productsearch.service;

import com.example.productsearch.dto.InventoryRequest;
import com.example.productsearch.dto.InventoryResponse;
import java.util.List;

public interface InventoryService {
	InventoryResponse createInventory(String productId, InventoryRequest request);

	InventoryResponse getProductInventory(String productId);

	List<InventoryResponse> getAllInventory(Integer page, Integer size);
}
