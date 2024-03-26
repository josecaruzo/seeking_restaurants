package com.fiap.seeking_restaurants_provider.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.seeking_restaurants_provider.controller.exception.ControllerExceptionHandler;
import com.fiap.seeking_restaurants_provider.dto.Review.ReviewDTO;
import com.fiap.seeking_restaurants_provider.dto.Review.ReviewRestaurantDTO;
import com.fiap.seeking_restaurants_provider.service.ReviewService;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import com.fiap.seeking_restaurants_provider.utils.ReviewUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReviewControllerTest {
	private MockMvc mockMvc;
	@Mock
	private ReviewService reviewService;
	AutoCloseable mocks;
	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		ReviewController reviewController = new ReviewController(reviewService);
		//mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
		mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
				.setControllerAdvice(new ControllerExceptionHandler())
				.addFilter((request, response, chain) -> {
					response.setCharacterEncoding("UTF-8");
					chain.doFilter(request, response);
				}, "/*")
				.build();
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}

	public static String asJsonString(final Object object){
		try{
			return new ObjectMapper().writeValueAsString(object);
		}catch(Exception e){
			throw new RuntimeException();
		}
	}

	@Nested
	class findReview {
		@Test
		void shouldFindByRestaurant() throws Exception {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var review1 = ReviewUtil.newReview((long)1, restaurant);
			var review2 = ReviewUtil.newReview((long)2, restaurant);

			var reviewList = Arrays.asList(
					ReviewRestaurantDTO.fromEntity(review1),
					ReviewRestaurantDTO.fromEntity(review2)
			);

			Mockito.when(reviewService.findByRestaurant(ArgumentMatchers.any(Long.class))).thenReturn(reviewList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders.get("/reviews/{restaurantId}", restaurant.getId()))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string(asJsonString(reviewList)));

			//Assertion
			Mockito.verify(reviewService, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Long.class));
		}

		@Test
		void shouldFindAll() throws Exception {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var review1 = ReviewUtil.newReview((long)1, restaurant);
			var review2 = ReviewUtil.newReview((long)2, restaurant);

			var reviewList = Arrays.asList(
					ReviewRestaurantDTO.fromEntity(review1),
					ReviewRestaurantDTO.fromEntity(review2)
			);
			Mockito.when(reviewService.findAll()).thenReturn(reviewList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders.get("/reviews"))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string(asJsonString(reviewList)));

			//Assertion
			Mockito.verify(reviewService, Mockito.times(1)).findAll();
		}
	}

	@Nested
	class addReview {
		@Test
		void shouldAdd() throws Exception {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var review = ReviewUtil.newReview((long)1, restaurant);
			var reviewDTO = ReviewRestaurantDTO.fromEntity(review);

			Mockito.when(reviewService.add(ArgumentMatchers.any(ReviewRestaurantDTO.class))).thenAnswer(i -> i.getArgument(0));

			//Act
			mockMvc.perform(MockMvcRequestBuilders.post("/reviews")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(reviewDTO))
			).andExpect(status().isCreated());

			//Assert
			Mockito.verify(reviewService, Mockito.times(1)).add(ArgumentMatchers.any(ReviewRestaurantDTO.class));
		}
	}

	@Nested
	class updateReview {
		@Test
		void shouldUpdate() throws Exception {
			//Arrange
			Long id = (long)1;
			var restaurant = RestaurantUtil.newRestaurant();
			var review = ReviewUtil.newReview(id, restaurant);
			var reviewDTO = ReviewDTO.fromEntity(review);

			Mockito.when(reviewService.update(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(ReviewDTO.class))).thenAnswer(i -> i.getArgument(1));

			//Act
			mockMvc.perform(MockMvcRequestBuilders.put("/reviews/{id}",id)
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(reviewDTO))
			)
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(asJsonString(reviewDTO)));

			//Assert
			Mockito.verify(reviewService, Mockito.times(1)).update(ArgumentMatchers.any(Long.class),ArgumentMatchers.any(ReviewDTO.class));
		}
	}

	@Nested
	class deleteReview {
		@Test
		void shouldDelete() throws Exception {
			//Arrange
			Long id = (long)1;
			Mockito.doNothing().when(reviewService).delete(ArgumentMatchers.any(Long.class));

			//Act
			mockMvc.perform(MockMvcRequestBuilders.delete("/reviews/{id}",id))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string("Review " + id + " deleted"));

			//Assert
			Mockito.verify(reviewService, Mockito.times(1)).delete(ArgumentMatchers.any(Long.class));
		}
	}
}
