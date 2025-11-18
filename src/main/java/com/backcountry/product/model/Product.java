package com.backcountry.product.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Product {
	private UUID id;
	private String name;
	private String description;
	private String brand;
	private BigDecimal price;
	private int inventory;
	private List<String> categories;
	private Instant createdAt;
	private Instant updatedAt;
}
