package com.backcountry.product.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Product details.
 * @param id
 * @param name
 * @param description
 * @param brand
 * @param price
 * @param inventory
 * @param categories
 * @param createdAt
 * @param updatedAt
 */
public record ProductResponse(
		UUID id,
		String name,
		String description,
		String brand,
		BigDecimal price,
		int inventory,
		List<String> categories,
		Instant createdAt,
		Instant updatedAt
) {}
