package com.example.productsearch.util;

import com.example.productsearch.dto.InventoryRequest;
import com.example.productsearch.dto.InventoryResponse;
import com.example.productsearch.dto.ProductRequest;
import com.example.productsearch.dto.ProductResponse;
import com.example.productsearch.elasticsearch.ProductSearchDocument;
import com.example.productsearch.model.Inventory;
import com.example.productsearch.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
	public Product toProduct(ProductRequest request) {
		Product product = new Product();
		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setBrand(request.getBrand());
		product.setCategory(request.getCategory());
		product.setPrice(request.getPrice());
		product.setActive(request.getActive() == null || request.getActive());
		return product;
	}

	public ProductResponse toProductResponse(Product product) {
		ProductResponse response = new ProductResponse();
		response.setId(product.getId());
		response.setName(product.getName());
		response.setDescription(product.getDescription());
		response.setBrand(product.getBrand());
		response.setCategory(product.getCategory());
		response.setPrice(product.getPrice());
		response.setActive(product.isActive());
		response.setCreatedAt(product.getCreatedAt());
		response.setUpdatedAt(product.getUpdatedAt());
		return response;
	}

	public ProductResponse toProductResponse(ProductSearchDocument document) {
		ProductResponse response = new ProductResponse();
		response.setId(document.getId());
		response.setName(document.getName());
		response.setDescription(document.getDescription());
		response.setBrand(document.getBrand());
		response.setCategory(document.getCategory());
		response.setPrice(document.getPrice());
		response.setActive(document.isActive());
		return response;
	}

	public ProductSearchDocument toProductSearchDocument(Product product) {
		ProductSearchDocument document = new ProductSearchDocument();
		document.setId(product.getId());
		document.setName(product.getName());
		document.setDescription(product.getDescription());
		document.setBrand(product.getBrand());
		document.setCategory(product.getCategory());
		document.setPrice(product.getPrice());
		document.setActive(product.isActive());
		return document;
	}

	public Inventory toInventory(String productId, InventoryRequest request) {
		Inventory inventory = new Inventory();
		inventory.setProductId(productId);
		inventory.setQuantity(request.getQuantity());
		inventory.setReservedQuantity(request.getReservedQuantity());
		inventory.setWarehouseLocation(request.getWarehouseLocation());
		return inventory;
	}

	public InventoryResponse toInventoryResponse(Inventory inventory) {
		InventoryResponse response = new InventoryResponse();
		response.setId(inventory.getId());
		response.setProductId(inventory.getProductId());
		response.setQuantity(inventory.getQuantity());
		response.setReservedQuantity(inventory.getReservedQuantity());
		response.setWarehouseLocation(inventory.getWarehouseLocation());
		response.setUpdatedAt(inventory.getUpdatedAt());
		return response;
	}
}
