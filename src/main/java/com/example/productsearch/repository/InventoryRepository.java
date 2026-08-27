package com.example.productsearch.repository;

import com.example.productsearch.model.Inventory;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
	Optional<Inventory> findByProductId(String productId);
}
