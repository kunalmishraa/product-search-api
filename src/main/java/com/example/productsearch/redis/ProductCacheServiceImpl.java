package com.example.productsearch.redis;

import com.example.productsearch.dto.ProductResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductCacheServiceImpl implements ProductCacheService {
	private static final Duration POPULAR_PRODUCTS_TTL = Duration.ofMinutes(10);

	private final StringRedisTemplate redisTemplate;
	private final RedisKeyBuilder redisKeyBuilder;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public ProductCacheServiceImpl(
			StringRedisTemplate redisTemplate,
			RedisKeyBuilder redisKeyBuilder) {
		this.redisTemplate = redisTemplate;
		this.redisKeyBuilder = redisKeyBuilder;
	}

	@Override
	public List<ProductResponse> getPopularProducts() {
		String value = redisTemplate.opsForValue().get(redisKeyBuilder.popularProductsKey());
		if (value == null) {
			return List.of();
		}
		try {
			return objectMapper.readValue(value, new TypeReference<>() {
			});
		} catch (Exception exception) {
			return List.of();
		}
	}

	@Override
	public void cachePopularProducts(List<ProductResponse> products) {
		try {
			String value = objectMapper.writeValueAsString(products);
			redisTemplate.opsForValue().set(redisKeyBuilder.popularProductsKey(), value, POPULAR_PRODUCTS_TTL);
		} catch (Exception ignored) {
		}
	}
}
