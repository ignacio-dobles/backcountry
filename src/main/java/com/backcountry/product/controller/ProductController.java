package com.backcountry.product.controller;

import com.backcountry.product.dto.CreateProductRequest;
import com.backcountry.product.dto.ProductResponse;
import com.backcountry.product.dto.UpdateProductRequest;
import com.backcountry.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}

	// ----------------------------------------
	// Create Product
	// POST /products
	// ----------------------------------------
	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
		ProductResponse created = service.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	// ----------------------------------------
	// Get Product by ID
	// GET /products/{id}
	// ----------------------------------------
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
		return service.getById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// ----------------------------------------
	// List Products
	// GET /products?brand=&category=&priceMin=&priceMax=&sort=&page=&size=
	// ----------------------------------------
	@GetMapping
	public ResponseEntity<List<ProductResponse>> list(
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) Double priceMin,
			@RequestParam(required = false) Double priceMax,
			@RequestParam(required = false) String sort,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(
				service.list(brand, category, priceMin, priceMax, sort, page, size)
		);
	}

	// ----------------------------------------
	// Update Product
	// PUT /products/{id}
	// ----------------------------------------
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> update(
			@PathVariable UUID id,
			@Valid @RequestBody UpdateProductRequest request
	) {
		return service.update(id, request)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	// ----------------------------------------
	// Delete Product
	// DELETE /products/{id}
	// ----------------------------------------
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		boolean removed = service.delete(id);
		return removed
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}
}
