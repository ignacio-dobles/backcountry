package com.backcountry.product.service;

import com.backcountry.product.dto.CreateProductRequest;
import com.backcountry.product.dto.ProductResponse;
import com.backcountry.product.dto.UpdateProductRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

	ProductResponse create(CreateProductRequest request);

	Optional<ProductResponse> getById(UUID id);

	List<ProductResponse> list(
			String brand,
			String category,
			Double priceMin,
			Double priceMax,
			String sort,
			int page,
			int size
	);

	Optional<ProductResponse> update(UUID id, UpdateProductRequest request);

	boolean delete(UUID id);
}
