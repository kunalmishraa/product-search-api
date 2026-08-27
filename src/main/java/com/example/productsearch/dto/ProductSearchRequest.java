package com.example.productsearch.dto;

import com.example.productsearch.model.ProductCategory;
import java.math.BigDecimal;

public class ProductSearchRequest {
	private String q;
	private ProductCategory category;
	private String brand;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Integer page;
	private Integer size;
	private String sort;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
