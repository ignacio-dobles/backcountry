package com.backcountry.product.exception;

import java.time.Instant;
import java.util.Map;

/**
 * API Error response structure.
 * @param timestamp
 * @param status
 * @param error
 * @param details
 */
public record ApiError(
		Instant timestamp,
		int status,
		String error,
		Map<String, String> details
) {}
