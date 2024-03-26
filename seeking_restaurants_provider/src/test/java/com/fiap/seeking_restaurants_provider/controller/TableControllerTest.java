package com.fiap.seeking_restaurants_provider.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.seeking_restaurants_provider.controller.exception.ControllerExceptionHandler;
import com.fiap.seeking_restaurants_provider.dto.Table.TableDTO;
import com.fiap.seeking_restaurants_provider.dto.Table.TableRestaurantDTO;
import com.fiap.seeking_restaurants_provider.service.TableService;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import com.fiap.seeking_restaurants_provider.utils.TableUtil;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TableControllerTest {
	private MockMvc mockMvc;
	@Mock
	private TableService tableService;
	AutoCloseable mocks;
	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		TableController tableController = new TableController(tableService);
		mockMvc = MockMvcBuilders.standaloneSetup(tableController)
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
	class findTable {
		@Test
		void shouldFindByRestaurant() throws Exception {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var table1 = TableUtil.newTable((long)1, 1, restaurant);
			var table2 = TableUtil.newTable((long)2, 2, restaurant);

			var tableList = Arrays.asList(
					TableRestaurantDTO.fromEntity(table1),
					TableRestaurantDTO.fromEntity(table2)
			);

			Mockito.when(tableService.findByRestaurant(ArgumentMatchers.any(Long.class))).thenReturn(tableList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders.get("/tables/{restaurantId}", restaurant.getId()))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string(asJsonString(tableList)));

			//Assertion
			Mockito.verify(tableService, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Long.class));
		}
	}

	@Nested
	class addTable {
		@Test
		void shouldAdd() throws Exception {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var table1 = TableUtil.newTable((long)1, 1, restaurant);
			var table2 = TableUtil.newTable((long)2, 2, restaurant);

			var tableList = Arrays.asList(
					TableRestaurantDTO.fromEntity(table1),
					TableRestaurantDTO.fromEntity(table2)
			);

			Mockito.when(tableService.addAll(ArgumentMatchers.any(Long.class))).thenReturn(tableList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders.post("/tables/all/{restaurantId}", restaurant.getId()))
					.andExpect(status().isCreated())
					.andExpect(MockMvcResultMatchers.content().string(asJsonString(tableList)));

			//Assert
			Mockito.verify(tableService, Mockito.times(1)).addAll(ArgumentMatchers.any(Long.class));
		}

		@Test
		void shouldAddAll() throws Exception {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var table = TableUtil.newTable((long)1, 1, restaurant);
			var tableDTO = TableRestaurantDTO.fromEntity(table);

			Mockito.when(tableService.add(ArgumentMatchers.any(TableRestaurantDTO.class))).thenAnswer(i -> i.getArgument(0));

			//Act
			mockMvc.perform(MockMvcRequestBuilders.post("/tables")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(tableDTO))
			).andExpect(status().isCreated());

			//Assert
			Mockito.verify(tableService, Mockito.times(1)).add(ArgumentMatchers.any(TableRestaurantDTO.class));
		}
	}

	@Nested
	class updateTable {
		@Test
		void shouldUpdate() throws Exception {
			//Arrange
			Long id = (long)1;
			var restaurant = RestaurantUtil.newRestaurant();
			var table = TableUtil.newTable(id, 1, restaurant);
			var tableDTO = TableDTO.fromEntity(table);

			Mockito.when(tableService.update(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(TableDTO.class))).thenAnswer(i -> i.getArgument(1));

			//Act
			mockMvc.perform(MockMvcRequestBuilders.put("/tables/{id}",id)
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(tableDTO)))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(asJsonString(tableDTO)));

			//Assert
			Mockito.verify(tableService, Mockito.times(1)).update(ArgumentMatchers.any(Long.class),ArgumentMatchers.any(TableDTO.class));
		}
	}

	@Nested
	class deleteTable {
		@Test
		void shouldDelete() throws Exception {
			//Arrange
			Long id = (long)1;
			Mockito.doNothing().when(tableService).delete(ArgumentMatchers.any(Long.class));

			//Act
			mockMvc.perform(MockMvcRequestBuilders.delete("/tables/{id}",id))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string("Table " + id + " deleted"));

			//Assert
			Mockito.verify(tableService, Mockito.times(1)).delete(ArgumentMatchers.any(Long.class));
		}
	}
}
