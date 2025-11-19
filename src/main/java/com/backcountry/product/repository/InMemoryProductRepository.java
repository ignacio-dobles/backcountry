package com.backcountry.product.repository;

import com.backcountry.product.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProductRepository implements ProductRepository {

	// Thread-safe storage
	private final Map<UUID, Product> store = new ConcurrentHashMap<>();

	@Override
	public Product save(Product product) {
		store.put(product.getId(), product);
		return product;
	}

	@Override
	public Optional<Product> findById(UUID id) {
		return Optional.ofNullable(store.get(id));
	}

	@Override
	public List<Product> findAll() {
		return List.copyOf(store.values());
	}

	@Override
	public void deleteById(UUID id) {
		store.remove(id);
	}

	@Override
	public void clear() {
		store.clear();
	}
}
