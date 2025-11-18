package com.backcountry.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

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
