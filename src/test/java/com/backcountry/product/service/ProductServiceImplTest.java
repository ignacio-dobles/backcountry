package com.backcountry.product.service;

import com.backcountry.product.dto.CreateProductRequest;
import com.backcountry.product.dto.UpdateProductRequest;
import com.backcountry.product.model.Product;
import com.backcountry.product.repository.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductServiceImplTest {

	private InMemoryProductRepository repository;
	private ProductServiceImpl service;

	@BeforeEach
	void setup() {
		repository = new InMemoryProductRepository();
		service = new ProductServiceImpl(repository);
	}

	// --------------------------------------------------------
	// CREATE
	// --------------------------------------------------------
	@Test
	void createProduct_success() {
		CreateProductRequest req = new CreateProductRequest(
				"Jacket",
				"Warm winter jacket",
				"NorthFace",
				new BigDecimal("199.99"),
				5,
				List.of("outerwear")
		);

		var created = service.create(req);

		assertNotNull(created.id());
		assertEquals("Jacket", created.name());
		assertEquals(new BigDecimal("199.99"), created.price());
		assertEquals(5, created.inventory());
		assertEquals(List.of("outerwear"), created.categories());
	}

	// --------------------------------------------------------
	// GET BY ID
	// --------------------------------------------------------
	@Test
	void getProduct_found() {
		Product p = Product.builder()
				.id(UUID.randomUUID())
				.name("Boots")
				.description("Hiking boots")
				.brand("Salomon")
				.price(new BigDecimal("149.99"))
				.inventory(10)
				.categories(List.of("footwear"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		repository.save(p);

		var result = service.getById(p.getId());

		assertTrue(result.isPresent());
		assertEquals("Boots", result.get().name());
	}

	@Test
	void getProduct_notFound_returnsEmpty() {
		var result = service.getById(UUID.randomUUID());
		assertTrue(result.isEmpty());
	}

	// --------------------------------------------------------
	// UPDATE
	// --------------------------------------------------------
	@Test
	void updateProduct_success() {
		Product existing = Product.builder()
				.id(UUID.randomUUID())
				.name("Tent")
				.description("2-person tent")
				.brand("REI")
				.price(new BigDecimal("199.99"))
				.inventory(3)
				.categories(List.of("tents"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		repository.save(existing);

		UpdateProductRequest req = new UpdateProductRequest(
				"Updated Tent",
				"3-person tent",
				"REI",
				new BigDecimal("249.99"),
				5,
				List.of("tents", "camping")
		);

		var result = service.update(existing.getId(), req);

		assertTrue(result.isPresent());
		assertEquals("Updated Tent", result.get().name());
		assertEquals(new BigDecimal("249.99"), result.get().price());
		assertEquals(5, result.get().inventory());
		assertEquals(List.of("tents", "camping"), result.get().categories());
	}

	@Test
	void updateProduct_notFound_returnsEmpty() {
		UpdateProductRequest req = new UpdateProductRequest(
				"X",
				"Y",
				"Brand",
				new BigDecimal("10"),
				1,
				List.of("cat")
		);

		var result = service.update(UUID.randomUUID(), req);

		assertTrue(result.isEmpty());
	}

	// --------------------------------------------------------
	// DELETE
	// --------------------------------------------------------
	@Test
	void deleteProduct_success() {
		Product p = Product.builder()
				.id(UUID.randomUUID())
				.name("Gloves")
				.description("Warm gloves")
				.brand("Patagonia")
				.price(new BigDecimal("49.99"))
				.inventory(5)
				.categories(List.of("gloves"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		repository.save(p);

		assertTrue(service.delete(p.getId()));
		assertTrue(repository.findById(p.getId()).isEmpty());
	}

	@Test
	void deleteProduct_notFound_returnsFalse() {
		assertFalse(service.delete(UUID.randomUUID()));
	}

	// --------------------------------------------------------
	// LIST (FILTERS, SORT, PAGINATION)
	// --------------------------------------------------------
	@Test
	void list_filterByBrand_success() {
		addSampleProducts();

		var result = service.list("Patagonia", null, null, null, null, 0, 10);

		assertEquals(1, result.size());
		assertEquals("Patagonia Jacket", result.get(0).name());
	}

	@Test
	void list_filterByCategory_success() {
		addSampleProducts();

		var result = service.list(null, "camping", null, null, null, 0, 10);
		assertEquals(1, result.size());
		assertEquals("Tent", result.get(0).name());
	}

	@Test
	void list_filterByPriceRange_success() {
		addSampleProducts();

		var result = service.list(null, null, 50.0, 150.0, null, 0, 10);
		assertEquals(1, result.size());
		assertEquals("Boots", result.get(0).name());
	}

	@Test
	void list_sortByPrice_success() {
		addSampleProducts();

		var result = service.list(null, null, null, null, "price", 0, 10);

		assertEquals("Boots", result.get(0).name());       // 99.99
		assertEquals("Tent", result.get(1).name());        // 199.99
		assertEquals("Patagonia Jacket", result.get(2).name());
	}

	@Test
	void list_pagination_success() {
		addSampleProducts();

		var page1 = service.list(null, null, null, null, "name", 0, 1);
		var page2 = service.list(null, null, null, null, "name", 1, 1);

		assertEquals(1, page1.size());
		assertEquals(1, page2.size());

		// Alphabetically
		assertEquals("Boots", page1.get(0).name());
		assertEquals("Patagonia Jacket", page2.get(0).name());
	}

	// --------------------------------------------------------
	// Helper to preload test data
	// --------------------------------------------------------
	private void addSampleProducts() {
		repository.save(Product.builder()
				.id(UUID.randomUUID())
				.name("Patagonia Jacket")
				.description("Warm jacket")
				.brand("Patagonia")
				.price(new BigDecimal("299.99"))
				.inventory(10)
				.categories(List.of("outerwear"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build());

		repository.save(Product.builder()
				.id(UUID.randomUUID())
				.name("Boots")
				.description("Hiking boots")
				.brand("Columbia")
				.price(new BigDecimal("99.99"))
				.inventory(4)
				.categories(List.of("footwear"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build());

		repository.save(Product.builder()
				.id(UUID.randomUUID())
				.name("Tent")
				.description("Camping tent")
				.brand("REI")
				.price(new BigDecimal("199.99"))
				.inventory(2)
				.categories(List.of("camping"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build());
	}
}
