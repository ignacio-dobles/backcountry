package com.backcountry.product.service;

import com.backcountry.product.dto.CreateProductRequest;
import com.backcountry.product.dto.UpdateProductRequest;
import com.backcountry.product.model.Product;
import com.backcountry.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

	@Mock
	private ProductRepository repository;

	@InjectMocks
	private ProductServiceImpl service;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	// --------------------------------------------------
	// CREATE
	// --------------------------------------------------
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

		when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		var created = service.create(req);

		assertNotNull(created.id());
		assertEquals("Jacket", created.name());
		verify(repository, times(1)).save(any());
	}

	// --------------------------------------------------
	// GET BY ID
	// --------------------------------------------------
	@Test
	void getProduct_found() {
		UUID id = UUID.randomUUID();

		Product product = Product.builder()
				.id(id)
				.name("Boots")
				.description("Hiking boots")
				.brand("Salomon")
				.price(new BigDecimal("149.99"))
				.inventory(10)
				.categories(List.of("footwear"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		when(repository.findById(id)).thenReturn(Optional.of(product));

		var result = service.getById(id);

		assertTrue(result.isPresent());
		assertEquals("Boots", result.get().name());
		verify(repository).findById(id);
	}

	@Test
	void getProduct_notFound() {
		UUID id = UUID.randomUUID();

		when(repository.findById(id)).thenReturn(Optional.empty());

		var result = service.getById(id);

		assertTrue(result.isEmpty());
	}

	// --------------------------------------------------
	// UPDATE
	// --------------------------------------------------
	@Test
	void updateProduct_success() {
		UUID id = UUID.randomUUID();

		Product existing = Product.builder()
				.id(id)
				.name("Tent")
				.description("2-person tent")
				.brand("REI")
				.price(new BigDecimal("199.99"))
				.inventory(3)
				.categories(List.of("tents"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		when(repository.findById(id)).thenReturn(Optional.of(existing));
		when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

		UpdateProductRequest req = new UpdateProductRequest(
				"Updated Tent",
				"3-person tent",
				"REI",
				new BigDecimal("249.99"),
				5,
				List.of("tents", "camping")
		);

		var result = service.update(id, req);

		assertTrue(result.isPresent());
		assertEquals("Updated Tent", result.get().name());
		verify(repository).save(any());
	}

	@Test
	void updateProduct_notFound() {
		UUID id = UUID.randomUUID();

		when(repository.findById(id)).thenReturn(Optional.empty());

		UpdateProductRequest req = new UpdateProductRequest(
				"X", "Y", "Brand", new BigDecimal("10"), 1, List.of("cat")
		);

		var result = service.update(id, req);

		assertTrue(result.isEmpty());
		verify(repository, never()).save(any());
	}

	// --------------------------------------------------
	// DELETE
	// --------------------------------------------------
	@Test
	void deleteProduct_success() {
		UUID id = UUID.randomUUID();

		Product existing = Product.builder()
				.id(id)
				.name("Gloves")
				.description("Winter gloves")
				.brand("Patagonia")
				.price(new BigDecimal("49.99"))
				.inventory(5)
				.categories(List.of("gloves"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		when(repository.findById(id)).thenReturn(Optional.of(existing));

		boolean result = service.delete(id);

		assertTrue(result);
		verify(repository).deleteById(id);
	}

	@Test
	void deleteProduct_notFound() {
		UUID id = UUID.randomUUID();

		when(repository.findById(id)).thenReturn(Optional.empty());

		boolean result = service.delete(id);

		assertFalse(result);
		verify(repository, never()).deleteById(any());
	}

	// --------------------------------------------------
	// LIST
	// --------------------------------------------------
	@Test
	void list_products_withFilters() {
		Product a = Product.builder()
				.id(UUID.randomUUID())
				.name("Tent")
				.description("Camping tent")
				.brand("REI")
				.price(new BigDecimal("199.99"))
				.inventory(2)
				.categories(List.of("camping"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		Product b = Product.builder()
				.id(UUID.randomUUID())
				.name("Boots")
				.description("Hiking boots")
				.brand("Columbia")
				.price(new BigDecimal("99.99"))
				.inventory(4)
				.categories(List.of("footwear"))
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		when(repository.findAll()).thenReturn(List.of(a, b));

		var result = service.list("Columbia", null, null, null, null, 0, 10);

		assertEquals(1, result.size());
		assertEquals("Boots", result.get(0).name());
	}
}
