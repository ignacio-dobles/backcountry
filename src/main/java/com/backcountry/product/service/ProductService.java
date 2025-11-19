package com.backcountry.product.service;

import com.backcountry.product.dto.CreateProductRequest;
import com.backcountry.product.dto.ProductResponse;
import com.backcountry.product.dto.UpdateProductRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing Products.
 */
public interface ProductService {

	/**
	 * Create a new Product
	 * @param request CreateProductRequest payload
	 * @return ProductResponse of the created product
	 */
	ProductResponse create(CreateProductRequest request);

	/**
	 * Get Product by ID
	 * @param id UUID of the product
	 * @return Optional containing ProductResponse if found, otherwise empty
	 */
	Optional<ProductResponse> getById(UUID id);

	/**
	 * List Products with optional filters, sorting, and pagination
	 * @param brand
	 * @param category
	 * @param priceMin
	 * @param priceMax
	 * @param sort
	 * @param page
	 * @param size
	 * @return
	 */
	List<ProductResponse> list(
			String brand,
			String category,
			Double priceMin,
			Double priceMax,
			String sort,
			int page,
			int size
	);

	/**
	 * Update an existing Product
	 * @param id UUID of the product to update
	 * @param request UpdateProductRequest payload
	 * @return Optional containing updated ProductResponse if found, otherwise empty
	 */
	Optional<ProductResponse> update(UUID id, UpdateProductRequest request);

	/**
	 * Delete a Product by ID
	 * @param id UUID of the product to delete
	 * @return true if the product was deleted, false if not found
	 */
	boolean delete(UUID id);
}
