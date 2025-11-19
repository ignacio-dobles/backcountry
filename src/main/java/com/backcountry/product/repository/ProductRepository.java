package com.backcountry.product.repository;

import com.backcountry.product.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

	Product save(Product product);

	Optional<Product> findById(UUID id);

	List<Product> findAll();

	void deleteById(UUID id);

	void clear();
}
