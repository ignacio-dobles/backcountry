package com.backcountry.product.service;

import com.backcountry.product.dto.CreateProductRequest;
import com.backcountry.product.dto.ProductResponse;
import com.backcountry.product.dto.UpdateProductRequest;
import com.backcountry.product.model.Product;
import com.backcountry.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository repository;

	public ProductServiceImpl(ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	public ProductResponse create(CreateProductRequest request) {
		Product product = Product.builder()
				.id(UUID.randomUUID())
				.name(request.name())
				.description(request.description())
				.brand(request.brand())
				.price(request.price())
				.inventory(request.inventory())
				.categories(request.categories())
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		repository.save(product);
		return toResponse(product);
	}

	@Override
	public Optional<ProductResponse> getById(UUID id) {
		return repository.findById(id).map(this::toResponse);
	}

	@Override
	public List<ProductResponse> list(String brand,
			String category,
			Double priceMin,
			Double priceMax,
			String sort,
			int page,
			int size) {

		List<Product> products = repository.findAll();

		// Filter
		return products.stream()
				.filter(p -> brand == null || p.getBrand().equalsIgnoreCase(brand))
				.filter(p -> category == null || p.getCategories().contains(category))
				.filter(p -> priceMin == null || p.getPrice().compareTo(BigDecimal.valueOf(priceMin)) >= 0)
				.filter(p -> priceMax == null || p.getPrice().compareTo(BigDecimal.valueOf(priceMax)) <= 0)

				// Sorting
				.sorted(getComparator(sort))

				// Pagination
				.skip((long) page * size)
				.limit(size)

				// Map to response
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ProductResponse> update(UUID id, UpdateProductRequest request) {
		return repository.findById(id).map(existing -> {

			Product updated = Product.builder()
					.id(existing.getId())
					.name(request.name())
					.description(request.description())
					.brand(request.brand())
					.price(request.price())
					.inventory(request.inventory())
					.categories(request.categories())
					.createdAt(existing.getCreatedAt())
					.updatedAt(Instant.now())
					.build();

			repository.save(updated);
			return toResponse(updated);
		});
	}

	@Override
	public boolean delete(UUID id) {
		return repository.findById(id).map(product -> {
			repository.deleteById(id);
			return true;
		}).orElse(false);
	}

	// -------------------------
	// Helpers
	// -------------------------

	private Comparator<Product> getComparator(String sort) {
		if (sort == null) return Comparator.comparing(Product::getCreatedAt).reversed();

		return switch (sort.toLowerCase()) {
			case "price"      -> Comparator.comparing(Product::getPrice);
			case "name"       -> Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER);
			case "brand"      -> Comparator.comparing(Product::getBrand, String.CASE_INSENSITIVE_ORDER);
			case "date"       -> Comparator.comparing(Product::getCreatedAt).reversed();
			default           -> Comparator.comparing(Product::getCreatedAt).reversed();
		};
	}

	private ProductResponse toResponse(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getName(),
				product.getDescription(),
				product.getBrand(),
				product.getPrice(),
				product.getInventory(),
				product.getCategories(),
				product.getCreatedAt(),
				product.getUpdatedAt()
		);
	}
}
