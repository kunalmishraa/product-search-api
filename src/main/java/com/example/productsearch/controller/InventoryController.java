package com.example.productsearch.controller;

import com.example.productsearch.dto.InventoryRequest;
import com.example.productsearch.dto.InventoryResponse;
import com.example.productsearch.service.InventoryService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {
	private final InventoryService inventoryService;

	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	@PostMapping("/api/products/{productId}/inventory")
	public InventoryResponse createInventory(
			@PathVariable String productId,
			@RequestBody InventoryRequest request) {
		return inventoryService.createInventory(productId, request);
	}

	@GetMapping("/api/products/{productId}/inventory")
	public InventoryResponse getProductInventory(@PathVariable String productId) {
		return inventoryService.getProductInventory(productId);
	}

	@GetMapping("/api/inventory")
	public List<InventoryResponse> getAllInventory(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		return inventoryService.getAllInventory(page, size);
	}
}
