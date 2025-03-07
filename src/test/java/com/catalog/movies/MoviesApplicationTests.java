package com.catalog.movies;

import com.catalog.movies.dto.LoginRequest;
import com.catalog.movies.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class MoviesApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static String adminToken;
	private static String userToken;
	private static Long createdMovieId;

	@Test
	@Order(1)
	public void testRegisterAdmin() throws Exception {
		RegisterRequest adminRequest = new RegisterRequest();
		adminRequest.setEmail("admin@test.com");
		adminRequest.setPassword("admin123");
		adminRequest.setRole("ADMIN");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(adminRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.email", is("admin@test.com")));
	}

	@Test
	@Order(2)
	public void testRegisterUser() throws Exception {
		RegisterRequest userRequest = new RegisterRequest();
		userRequest.setEmail("user@test.com");
		userRequest.setPassword("user123");
		userRequest.setRole("USER");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.email", is("user@test.com")));
	}

	@Test
	@Order(3)
	public void testLoginAdmin() throws Exception {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("admin@test.com");
		loginRequest.setPassword("admin123");

		MvcResult result = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").exists())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		adminToken = objectMapper.readTree(response).get("accessToken").asText();
	}

	@Test
	@Order(4)
	public void testLoginUser() throws Exception {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("user@test.com");
		loginRequest.setPassword("user123");

		MvcResult result = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(loginRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").exists())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		userToken = objectMapper.readTree(response).get("accessToken").asText();
	}

	@Test
	@Order(5)
	public void testCreateMovie() throws Exception {
		String movieJson = "{\n" +
				"  \"name\": \"Test Movie\",\n" +
				"  \"releaseYear\": 2022,\n" +
				"  \"synopsis\": \"A test movie synopsis.\",\n" +
				"  \"categories\": [\"Action\", \"Drama\"],\n" +
				"  \"posterUrl\": \"http://example.com/poster.jpg\"\n" +
				"}";

		MvcResult result = mockMvc.perform(post("/api/movies")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + adminToken)
						.content(movieJson))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is("Test Movie")))
				.andReturn();

		String response = result.getResponse().getContentAsString();
		createdMovieId = objectMapper.readTree(response).get("id").asLong();
	}

	@Test
	@Order(6)
	public void testUpdateMovie() throws Exception {
		String updatedMovieJson = "{\n" +
				"  \"name\": \"Updated Test Movie\",\n" +
				"  \"releaseYear\": 2023,\n" +
				"  \"synopsis\": \"Updated synopsis.\",\n" +
				"  \"categories\": [\"Comedy\"],\n" +
				"  \"posterUrl\": \"http://example.com/updated.jpg\"\n" +
				"}";

		mockMvc.perform(put("/api/movies/" + createdMovieId)
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + adminToken)
						.content(updatedMovieJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Updated Test Movie")));
	}

	@Test
	@Order(7)
	public void testGetMovies() throws Exception {
		mockMvc.perform(get("/api/movies")
						.param("search", "Test")
						.param("page", "0")
						.param("size", "10")
						.param("sortBy", "createdDate")
						.param("sortDir", "desc")
						.header("Authorization", "Bearer " + adminToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray());
	}

	@Test
	@Order(8)
	public void testDeleteMovie() throws Exception {
		mockMvc.perform(delete("/api/movies/" + createdMovieId)
						.header("Authorization", "Bearer " + adminToken))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(9)
	public void testRateMovie() throws Exception {
		String movieJson = "{\n" +
				"  \"name\": \"Movie to Rate\",\n" +
				"  \"releaseYear\": 2022,\n" +
				"  \"synopsis\": \"A movie to be rated.\",\n" +
				"  \"categories\": [\"Action\"],\n" +
				"  \"posterUrl\": \"http://example.com/poster_rate.jpg\"\n" +
				"}";
		MvcResult result = mockMvc.perform(post("/api/movies")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + adminToken)
						.content(movieJson))
				.andExpect(status().isCreated())
				.andReturn();
		Long movieToRateId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

		mockMvc.perform(post("/api/ratings/" + movieToRateId)
						.param("rating", "4")
						.header("Authorization", "Bearer " + userToken))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.rating", is(4)));
	}
}