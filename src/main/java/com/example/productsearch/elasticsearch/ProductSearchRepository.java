package com.example.productsearch.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductSearchDocument, String> {
}
