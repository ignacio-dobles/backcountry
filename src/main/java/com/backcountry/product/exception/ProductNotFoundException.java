package com.backcountry.product.exception;

/**
 * Exception thrown when a Product is not found.
 */

public class ProductNotFoundException extends RuntimeException {
	public ProductNotFoundException(String id) {
		super("Product with id " + id + " not found");
	}
}
