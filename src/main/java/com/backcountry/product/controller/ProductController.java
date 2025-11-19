package com.backcountry.product.controller;

import com.backcountry.product.dto.CreateProductRequest;
import com.backcountry.product.dto.ProductResponse;
import com.backcountry.product.dto.UpdateProductRequest;
import com.backcountry.product.exception.ProductNotFoundException;
import com.backcountry.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing products.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService service;

	public ProductController(ProductService service) {
		this.service = service;
	}

	/**
	 * Create a new Product
	 * @param request CreateProductRequest payload
	 * @return ResponseEntity with created ProductResponse
	 */
	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
		ProductResponse created = service.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	/**
	 * Get Product by ID
	 * @param id UUID of the product
	 * @return ProductResponse
	 */
	@GetMapping("/{id}")
	public ProductResponse getById(@PathVariable UUID id) {
		return service.getById(id)
				.orElseThrow(() -> new ProductNotFoundException(id.toString()));
	}

	/**
	 * List Products with optional filters, sorting, and pagination
	 *
	 * @param brand brand filter
	 * @param category category filter
	 * @param priceMin minimum price filter
	 * @param priceMax maximum price filter
	 * @param sort sort order
	 * @param page page number
	 * @param size page size
	 * @return List of ProductResponse
	 */
	@GetMapping
	public List<ProductResponse> list(
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) Double priceMin,
			@RequestParam(required = false) Double priceMax,
			@RequestParam(required = false) String sort,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return service.list(brand, category, priceMin, priceMax, sort, page, size);
	}

	/**
	 * Update Product by ID
	 * @param id UUID of the product
	 * @param request UpdateProductRequest payload
	 * @return Updated ProductResponse
	 */
	@PutMapping("/{id}")
	public ProductResponse update(
			@PathVariable UUID id,
			@Valid @RequestBody UpdateProductRequest request
	) {
		return service.update(id, request)
				.orElseThrow(() -> new ProductNotFoundException(id.toString()));
	}

	/**
	 * Delete Product by ID
	 * @param id UUID of the product
	 * @return ResponseEntity with no content
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		boolean removed = service.delete(id);

		if (!removed) {
			throw new ProductNotFoundException(id.toString());
		}

		return ResponseEntity.noContent().build();
	}
}
