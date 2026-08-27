package com.example.productsearch.exception;

public class InventoryNotFoundException extends RuntimeException {
	public InventoryNotFoundException(String productId) {
		super("Inventory not found for product: " + productId);
	}
}
