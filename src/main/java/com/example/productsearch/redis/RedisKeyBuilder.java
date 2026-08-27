package com.example.productsearch.redis;

import org.springframework.stereotype.Component;

@Component
public class RedisKeyBuilder {
	private static final String PRODUCT_PREFIX = "product";

	public String popularProductsKey() {
		return PRODUCT_PREFIX + ":popular";
	}
}
