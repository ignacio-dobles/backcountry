package com.backcountry.product.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(

		@NotBlank
		String name,

		@NotBlank
		String description,

		@NotBlank
		String brand,

		@NotNull
		@Positive
		BigDecimal price,

		@Min(0)
		int inventory,

		@NotNull
		@Size(min = 1)
		List<@NotBlank String> categories
) {}
