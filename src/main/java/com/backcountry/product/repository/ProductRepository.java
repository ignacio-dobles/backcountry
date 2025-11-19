package com.backcountry.product.repository;

import com.backcountry.product.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Product entity.
 */
public interface ProductRepository {

	/**
	 * Save a product.
	 * @param product the product to save
	 * @return the saved product
	 */
	Product save(Product product);

	/**
	 * Find a product by its ID.
	 * @param id the UUID of the product
	 * @return an Optional containing the found product or empty if not found
	 */
	Optional<Product> findById(UUID id);

	/**
	 * Find all products.
	 * @return a list of all products
	 */
	List<Product> findAll();

	/**
	 * Delete a product by its ID.
	 * @param id the UUID of the product to delete
	 */
	void deleteById(UUID id);

	/**
	 * Clear all products from the repository.
	 */
	void clear();
}
