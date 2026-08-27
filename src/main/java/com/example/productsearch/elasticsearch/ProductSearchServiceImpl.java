package com.example.productsearch.elasticsearch;

import com.example.productsearch.model.ProductCategory;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Service;

@Service("productSearchIndexService")
public class ProductSearchServiceImpl implements ProductSearchService {
	private final ProductSearchRepository productSearchRepository;

	public ProductSearchServiceImpl(ProductSearchRepository productSearchRepository) {
		this.productSearchRepository = productSearchRepository;
	}

	@Override
	public void indexProduct(ProductSearchDocument document) {
		productSearchRepository.save(document);
	}

	@Override
	public List<ProductSearchDocument> search(
			String q,
			ProductCategory category,
			String brand,
			BigDecimal minPrice,
			BigDecimal maxPrice,
			int page,
			int size,
			String sort) {
		List<ProductSearchDocument> matches = StreamSupport.stream(productSearchRepository.findAll().spliterator(), false)
				.filter(product -> matchesText(product, q))
				.filter(product -> category == null || category == product.getCategory())
				.filter(product -> isBlank(brand) || brand.equalsIgnoreCase(product.getBrand()))
				.filter(product -> minPrice == null || product.getPrice() != null && product.getPrice().compareTo(minPrice) >= 0)
				.filter(product -> maxPrice == null || product.getPrice() != null && product.getPrice().compareTo(maxPrice) <= 0)
				.sorted(comparator(sort))
				.toList();

		int fromIndex = Math.min(page * size, matches.size());
		int toIndex = Math.min(fromIndex + size, matches.size());
		return matches.subList(fromIndex, toIndex);
	}

	private boolean matchesText(ProductSearchDocument product, String q) {
		if (isBlank(q)) {
			return true;
		}
		String normalized = q.toLowerCase(Locale.ROOT);
		return contains(product.getName(), normalized)
				|| contains(product.getDescription(), normalized)
				|| contains(product.getBrand(), normalized);
	}

	private boolean contains(String value, String q) {
		return value != null && value.toLowerCase(Locale.ROOT).contains(q);
	}

	private boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	private Comparator<ProductSearchDocument> comparator(String sort) {
		Comparator<ProductSearchDocument> comparator = Comparator.comparing(ProductSearchDocument::getName, Comparator.nullsLast(String::compareToIgnoreCase));
		if (sort == null || sort.isBlank()) {
			return comparator;
		}
		String[] parts = sort.split(",");
		if ("price".equalsIgnoreCase(parts[0])) {
			comparator = Comparator.comparing(ProductSearchDocument::getPrice, Comparator.nullsLast(BigDecimal::compareTo));
		}
		if (parts.length > 1 && "desc".equalsIgnoreCase(parts[1])) {
			return comparator.reversed();
		}
		return comparator;
	}
}
