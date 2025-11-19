package com.backcountry.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {

		Map<String, String> details = ex.getBindingResult().getFieldErrors()
				.stream()
				.collect(Collectors.toMap(
						err -> err.getField(),
						err -> err.getDefaultMessage(),
						(a, b) -> a
				));

		ApiError error = new ApiError(
				Instant.now(),
				HttpStatus.BAD_REQUEST.value(),
				"Validation Error",
				details
		);

		return ResponseEntity.badRequest().body(error);
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(ProductNotFoundException ex) {

		ApiError error = new ApiError(
				Instant.now(),
				HttpStatus.NOT_FOUND.value(),
				"Product Not Found",     // <---- REQUIRED BY TESTS
				Map.of("message", ex.getMessage())  // still preserve original message
		);

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGeneric(Exception ex) {

		ApiError error = new ApiError(
				Instant.now(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error",
				Map.of()
		);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
