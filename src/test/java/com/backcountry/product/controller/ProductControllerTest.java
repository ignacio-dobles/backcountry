package com.backcountry.product.controller;

import com.backcountry.product.dto.CreateProductRequest;
import com.backcountry.product.dto.ProductResponse;
import com.backcountry.product.dto.UpdateProductRequest;
import com.backcountry.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private ProductService service;

	// --------------------------------------------------------------------
	// POST /products
	// --------------------------------------------------------------------
	@Test
	void createProduct_returns201() throws Exception {
		CreateProductRequest req = new CreateProductRequest(
				"Jacket", "Warm", "NorthFace",
				new BigDecimal("199.99"), 5, List.of("outerwear")
		);

		ProductResponse resp = new ProductResponse(
				UUID.randomUUID(), "Jacket", "Warm", "NorthFace",
				new BigDecimal("199.99"), 5, List.of("outerwear"),
				Instant.now(), Instant.now()
		);

		when(service.create(any())).thenReturn(resp);

		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(req)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Jacket"));
	}

	// --------------------------------------------------------------------
	// GET /products/{id}
	// --------------------------------------------------------------------
	@Test
	void getProductById_found_returns200() throws Exception {
		UUID id = UUID.randomUUID();

		ProductResponse resp = new ProductResponse(
				id, "Boots", "Hiking boots", "Salomon",
				new BigDecimal("149.99"), 10, List.of("footwear"),
				Instant.now(), Instant.now()
		);

		when(service.getById(id)).thenReturn(Optional.of(resp));

		mockMvc.perform(get("/products/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Boots"));
	}

	@Test
	void getProductById_notFound_returns404() throws Exception {
		UUID id = UUID.randomUUID();

		when(service.getById(id)).thenReturn(Optional.empty());

		mockMvc.perform(get("/products/" + id))
				.andExpect(status().isNotFound());
	}

	// --------------------------------------------------------------------
	// GET /products with filters
	// --------------------------------------------------------------------
	@Test
	void listProducts_returns200() throws Exception {
		ProductResponse resp = new ProductResponse(
				UUID.randomUUID(), "Tent", "Camping tent", "REI",
				new BigDecimal("199.99"), 2, List.of("camping"),
				Instant.now(), Instant.now()
		);

		when(service.list(any(), any(), any(), any(), any(), anyInt(), anyInt()))
				.thenReturn(List.of(resp));

		mockMvc.perform(get("/products?brand=REI"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].brand").value("REI"));
	}

	// --------------------------------------------------------------------
	// PUT /products/{id}
	// --------------------------------------------------------------------
	@Test
	void updateProduct_found_returns200() throws Exception {
		UUID id = UUID.randomUUID();

		UpdateProductRequest req = new UpdateProductRequest(
				"Updated Tent", "3-person tent", "REI",
				new BigDecimal("249.99"), 5, List.of("camping")
		);

		ProductResponse resp = new ProductResponse(
				id, "Updated Tent", "3-person tent", "REI",
				new BigDecimal("249.99"), 5, List.of("camping"),
				Instant.now(), Instant.now()
		);

		when(service.update(eq(id), any())).thenReturn(Optional.of(resp));

		mockMvc.perform(put("/products/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Tent"));
	}

	@Test
	void updateProduct_notFound_returns404() throws Exception {
		UUID id = UUID.randomUUID();

		UpdateProductRequest req = new UpdateProductRequest(
				"X", "Y", "Brand",
				new BigDecimal("10"), 1, List.of("cat")
		);

		when(service.update(eq(id), any())).thenReturn(Optional.empty());

		mockMvc.perform(put("/products/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(req)))
				.andExpect(status().isNotFound());
	}

	// --------------------------------------------------------------------
	// DELETE /products/{id}
	// --------------------------------------------------------------------
	@Test
	void deleteProduct_success_returns204() throws Exception {
		UUID id = UUID.randomUUID();

		when(service.delete(id)).thenReturn(true);

		mockMvc.perform(delete("/products/" + id))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteProduct_notFound_returns404() throws Exception {
		UUID id = UUID.randomUUID();

		when(service.delete(id)).thenReturn(false);

		mockMvc.perform(delete("/products/" + id))
				.andExpect(status().isNotFound());
	}
}
