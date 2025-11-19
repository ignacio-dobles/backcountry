package com.backcountry.product.repository;

import com.backcountry.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryProductRepositoryTest {

	private InMemoryProductRepository repository;

	@BeforeEach
	void setup() {
		repository = new InMemoryProductRepository();
	}

	private Product buildProduct(UUID id) {
		return Product.builder()
				.id(id)
				.name("Tent")
				.description("3-person tent")
				.brand("REI")
				.price(new BigDecimal("199.99"))
				.inventory(5)
				.categories(List.of("camping"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();
	}

	// --------------------------------------------------------------------
	// save()
	// --------------------------------------------------------------------
	@Test
	void save_storesProductCorrectly() {
		UUID id = UUID.randomUUID();
		Product product = buildProduct(id);

		Product saved = repository.save(product);

		assertEquals(product, saved);
		assertEquals(product, repository.findById(id).orElse(null));
	}

	// --------------------------------------------------------------------
	// findById()
	// --------------------------------------------------------------------
	@Test
	void findById_existingProduct_returnsProduct() {
		UUID id = UUID.randomUUID();
		Product product = buildProduct(id);

		repository.save(product);

		Optional<Product> result = repository.findById(id);

		assertTrue(result.isPresent());
		assertEquals("Tent", result.get().getName());
	}

	@Test
	void findById_missingProduct_returnsEmptyOptional() {
		Optional<Product> result = repository.findById(UUID.randomUUID());

		assertTrue(result.isEmpty());
	}

	// --------------------------------------------------------------------
	// findAll()
	// --------------------------------------------------------------------
	@Test
	void findAll_returnsAllProducts() {
		Product p1 = buildProduct(UUID.randomUUID());
		Product p2 = buildProduct(UUID.randomUUID());

		repository.save(p1);
		repository.save(p2);

		List<Product> all = repository.findAll();

		assertEquals(2, all.size());
		assertTrue(all.contains(p1));
		assertTrue(all.contains(p2));
	}

	// --------------------------------------------------------------------
	// deleteById()
	// --------------------------------------------------------------------
	@Test
	void deleteById_removesProduct() {
		UUID id = UUID.randomUUID();
		Product product = buildProduct(id);

		repository.save(product);
		repository.deleteById(id);

		assertTrue(repository.findById(id).isEmpty());
	}
}
