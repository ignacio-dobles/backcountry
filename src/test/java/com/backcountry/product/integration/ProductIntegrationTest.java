package com.backcountry.product.integration;

import com.backcountry.product.dto.CreateProductRequest;
import com.backcountry.product.dto.UpdateProductRequest;
import com.backcountry.product.repository.InMemoryProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private InMemoryProductRepository repository;

	@BeforeEach
	void setup() throws Exception {
		repository.clear();

		createProduct("Nano Jacket", "Patagonia", 199.99, List.of("jackets"));
		createProduct("Trail Boots", "Salomon", 149.99, List.of("footwear"));
		createProduct("Tent", "REI", 249.99, List.of("camping"));
	}

	// Helper
	private void createProduct(String name, String brand, double price, List<String> cat) throws Exception {
		CreateProductRequest req = new CreateProductRequest(
				name,
				name + " desc",
				brand,
				BigDecimal.valueOf(price),
				5,
				cat
		);

		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isCreated());
	}

	@Test
	void getProducts_returnsAllInsertedProducts() throws Exception {
		mockMvc.perform(get("/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(3)));
	}

	@Test
	void getProducts_filterByBrand() throws Exception {
		mockMvc.perform(get("/products?brand=Salomon"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(1)))
				.andExpect(jsonPath("$[0].brand", is("Salomon")));
	}

	@Test
	void getProducts_filterByCategory() throws Exception {
		mockMvc.perform(get("/products?category=jackets"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(1)))
				.andExpect(jsonPath("$[0].name", is("Nano Jacket")));
	}

	@Test
	void getProducts_sortedByPriceAscending() throws Exception {
		mockMvc.perform(get("/products?sort=price"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].price", is(149.99)))
				.andExpect(jsonPath("$[1].price", is(199.99)))
				.andExpect(jsonPath("$[2].price", is(249.99)));
	}

	@Test
	void getProduct_notFound_returns404AndErrorJson() throws Exception {
		UUID missing = UUID.randomUUID();

		mockMvc.perform(get("/products/" + missing))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error", is("Product Not Found")));
	}

	@Test
	void deleteProduct_notFound_returns404AndErrorJson() throws Exception {
		UUID missing = UUID.randomUUID();

		mockMvc.perform(delete("/products/" + missing))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error", is("Product Not Found")));
	}

	@Test
	void updateProduct_notFound_returns404AndErrorJson() throws Exception {
		UUID missing = UUID.randomUUID();

		UpdateProductRequest req = new UpdateProductRequest(
				"New",
				"New desc",
				"Brand",
				BigDecimal.valueOf(50),
				5,
				List.of("x")
		);

		mockMvc.perform(put("/products/" + missing)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error", is("Product Not Found")));
	}
}
