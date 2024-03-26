package com.fiap.seeking_restaurants_provider.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.seeking_restaurants_provider.controller.exception.ControllerExceptionHandler;
import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarRestaurantDTO;
import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantCalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantDTO;
import com.fiap.seeking_restaurants_provider.service.CalendarService;
import com.fiap.seeking_restaurants_provider.service.RestaurantService;
import com.fiap.seeking_restaurants_provider.utils.CalendarUtil;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
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
import java.util.Calendar;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerTest {

	private MockMvc mockMvc;
	@Mock
	private RestaurantService restaurantService;

	@Mock
	private CalendarService calendarService;
	AutoCloseable mocks;
	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		RestaurantController restaurantController = new RestaurantController(restaurantService, calendarService);
		//mockMvc = MockMvcBuilders.standaloneSetup(restaurantController).build();
		mockMvc = MockMvcBuilders.standaloneSetup(restaurantController)
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
	class findRestaurant {

		@Test
		void shouldFindByName() throws Exception {
			//Arrange
			var calendar = CalendarUtil.newCalendar();
			var restaurant1 = RestaurantUtil.newRestaurant((long) 1, calendar);
			var restaurant2 = RestaurantUtil.newRestaurant((long) 2, calendar);

			var restaurantList = Arrays.asList(
					RestaurantCalendarDTO.fromEntity(restaurant1),
					RestaurantCalendarDTO.fromEntity(restaurant2)
			);

			Mockito.when(restaurantService.findByName(ArgumentMatchers.any(String.class))).thenReturn(restaurantList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/name/{name}", restaurant1.getName()))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string(asJsonString(restaurantList)));

			//Assertion
			Mockito.verify(restaurantService, Mockito.times(1)).findByName(ArgumentMatchers.any(String.class));
		}

		@Test
		void shouldFindByAddress() throws Exception {
			//Arrange
			var calendar = CalendarUtil.newCalendar();
			var restaurant1 = RestaurantUtil.newRestaurant((long) 1, calendar);
			var restaurant2 = RestaurantUtil.newRestaurant((long) 2, calendar);

			var restaurantList = Arrays.asList(
					RestaurantCalendarDTO.fromEntity(restaurant1),
					RestaurantCalendarDTO.fromEntity(restaurant2)
			);

			Mockito.when(restaurantService.findByAddress(ArgumentMatchers.any(String.class))).thenReturn(restaurantList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/location/{address}", restaurant1.getAddress()))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string(asJsonString(restaurantList)));

			//Assertion
			Mockito.verify(restaurantService, Mockito.times(1)).findByAddress(ArgumentMatchers.any(String.class));
		}

		@Test
		void shouldFindByType() throws Exception {
			//Arrange
			var calendar = CalendarUtil.newCalendar();
			var restaurant1 = RestaurantUtil.newRestaurant((long) 1, calendar);
			var restaurant2 = RestaurantUtil.newRestaurant((long) 2, calendar);

			var restaurantList = Arrays.asList(
					RestaurantCalendarDTO.fromEntity(restaurant1),
					RestaurantCalendarDTO.fromEntity(restaurant2)
			);

			Mockito.when(restaurantService.findByType(ArgumentMatchers.any(String.class))).thenReturn(restaurantList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/type/{type}", restaurant1.getType()))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string(asJsonString(restaurantList)));

			//Assertion
			Mockito.verify(restaurantService, Mockito.times(1)).findByType(ArgumentMatchers.any(String.class));
		}

		@Test
		void shouldFindAll() throws Exception {
			//Arrange
			var calendar = CalendarUtil.newCalendar();
			var restaurant1 = RestaurantUtil.newRestaurant((long) 1, calendar);
			var restaurant2 = RestaurantUtil.newRestaurant((long) 2, calendar);

			var restaurantList = Arrays.asList(
					RestaurantCalendarDTO.fromEntity(restaurant1),
					RestaurantCalendarDTO.fromEntity(restaurant2)
			);

			Mockito.when(restaurantService.findAll()).thenReturn(restaurantList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/"))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string(asJsonString(restaurantList)));

			//Assertion
			Mockito.verify(restaurantService, Mockito.times(1)).findAll();
		}
	}

	@Nested
	class addRestaurant {
		@Test
		void shouldAdd() throws Exception {
			//Arrange
			var calendar = CalendarUtil.newCalendar();
			var restaurant = RestaurantUtil.newRestaurant((long)1,calendar);
			calendar.setRestaurant(restaurant);

			var restaurantDTO = RestaurantCalendarDTO.fromEntity(restaurant);

			Mockito.when(calendarService.add(ArgumentMatchers.any(CalendarRestaurantDTO.class))).thenAnswer(i -> i.getArgument(0));
			Mockito.when(restaurantService.add(ArgumentMatchers.any(RestaurantDTO.class))).thenAnswer(i -> i.getArgument(0));

			//Act
			mockMvc.perform(MockMvcRequestBuilders.post("/restaurants")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(restaurantDTO))
			).andExpect(status().isCreated());

			//Assert
			Mockito.verify(calendarService, Mockito.times(1)).add(ArgumentMatchers.any(CalendarRestaurantDTO.class));
			Mockito.verify(restaurantService, Mockito.times(1)).add(ArgumentMatchers.any(RestaurantDTO.class));
		}
	}

	@Nested
	class updateRestaurant {
		@Test
		void shouldUpdate() throws Exception {
			//Arrange
			Long id = (long)1;
			var calendar = CalendarUtil.newCalendar();
			var restaurant = RestaurantUtil.newRestaurant(id, calendar);
			calendar.setRestaurant(restaurant);
			var restaurantDTO = RestaurantCalendarDTO.fromEntity(restaurant);

			Mockito.when(restaurantService.update(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(RestaurantDTO.class))).thenAnswer(i -> i.getArgument(1));
			Mockito.when(calendarService.update(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(CalendarDTO.class))).thenAnswer(i -> i.getArgument(1));

			//Act
			mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/{id}",id)
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(restaurantDTO))
			)
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(asJsonString(restaurantDTO)));

			//Assert
			Mockito.verify(restaurantService, Mockito.times(1)).update(ArgumentMatchers.any(Long.class),ArgumentMatchers.any(RestaurantDTO.class));
			Mockito.verify(calendarService, Mockito.times(1)).update(ArgumentMatchers.any(Long.class),ArgumentMatchers.any(CalendarDTO.class));
		}
	}

	@Nested
	class deleteRestaurant {
		@Test
		void shouldDelete() throws Exception {
			//Arrange
			Long id = (long) 1;
			Mockito.doNothing().when(restaurantService).delete(ArgumentMatchers.any(Long.class));

			//Act
			mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/{id}", id))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string("Restaurant " + id + " deleted"));

			//Assert
			Mockito.verify(restaurantService, Mockito.times(1)).delete(ArgumentMatchers.any(Long.class));
		}
	}
}
