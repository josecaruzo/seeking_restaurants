package com.fiap.seeking_restaurants_provider.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fiap.seeking_restaurants_provider.controller.exception.ControllerExceptionHandler;
import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantTableDTO;
import com.fiap.seeking_restaurants_provider.service.ReservationService;
import com.fiap.seeking_restaurants_provider.utils.*;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationControllerTest {

	private MockMvc mockMvc;
	@Mock
	private ReservationService reservationService;

	AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		ReservationController reservationController = new ReservationController(reservationService);
		mockMvc = MockMvcBuilders.standaloneSetup(reservationController)
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
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		try{
			return mapper.writeValueAsString(object);
		}catch(Exception e){
			System.out.println(e.toString());
			throw new RuntimeException();
		}
	}
	@Nested
	class findReservation {
		@Test
		void shouldFindByRestaurant() throws Exception {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var reservation1 = ReservationUtil.newReservation((long)1,restaurant);
			var reservation2 = ReservationUtil.newReservation((long)2,restaurant);

			var reservationTableList = Arrays.asList(
					ReservationRestaurantTableDTO.fromEntity(reservation1),
					ReservationRestaurantTableDTO.fromEntity(reservation2)
			);

			Mockito.when(reservationService.findByRestaurant(ArgumentMatchers.any(Long.class))).thenReturn(reservationTableList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders.get("/reservations/restaurant/{restaurantId}", restaurant.getId()))
					.andExpect(status().isOk());

			//Assertion
			Mockito.verify(reservationService, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Long.class));
		}

		@Test
		void shouldFindByRestaurantAndReservationDate() throws Exception {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var reservation1 = ReservationUtil.newReservation((long)1,restaurant);
			var reservation2 = ReservationUtil.newReservation((long)2,restaurant);


			var reservationTableList = Arrays.asList(
					ReservationRestaurantTableDTO.fromEntity(reservation1),
					ReservationRestaurantTableDTO.fromEntity(reservation2)
			);

			Mockito.when(reservationService.findByRestaurantAndReservationDate(
					ArgumentMatchers.any(Long.class),
					ArgumentMatchers.any(LocalDate.class))).thenReturn(reservationTableList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders
					.get("/reservations/restaurant/{restaurantId}/date/{reservationDate}", restaurant.getId(), reservation1.getReservationDate()))
						.andExpect(status().isOk());

			//Assertion
			Mockito.verify(reservationService, Mockito.times(1)).findByRestaurantAndReservationDate(
					ArgumentMatchers.any(Long.class),
					ArgumentMatchers.any(LocalDate.class));
		}

		@Test
		void shouldFindByRestaurantAndReservationDateAndReservationHour() throws Exception {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var reservation1 = ReservationUtil.newReservation((long)1,restaurant);
			var reservation2 = ReservationUtil.newReservation((long)2,restaurant);


			var reservationTableList = Arrays.asList(
					ReservationRestaurantTableDTO.fromEntity(reservation1),
					ReservationRestaurantTableDTO.fromEntity(reservation2)
			);

			var asJson = asJsonString(reservationTableList);

			Mockito.when(reservationService.findByRestaurantAndReservationDateAndReservationHour(
					ArgumentMatchers.any(Long.class),
					ArgumentMatchers.any(LocalDate.class),
					ArgumentMatchers.any(LocalTime.class))).thenReturn(reservationTableList);

			//Act
			mockMvc.perform(MockMvcRequestBuilders
							.get("/reservations/restaurant/{restaurantId}/date/{reservationDate}/hour/{reservationHour}",
									restaurant.getId(),
									reservation1.getReservationDate(),
									reservation1.getReservationHour())
			).andExpect(status().isOk());

			//Assertion
			Mockito.verify(reservationService, Mockito.times(1)).findByRestaurantAndReservationDateAndReservationHour(
					ArgumentMatchers.any(Long.class),
					ArgumentMatchers.any(LocalDate.class),
					ArgumentMatchers.any(LocalTime.class));
		}
	}

	@Nested
	class addReservation {
		@Test
		void shouldAdd() throws Exception {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var reservation = ReservationUtil.newReservation((long)1, restaurant);
			var table = TableUtil.newTable((long) 1, 1, restaurant);
			reservation.setTables(Set.of(table));

			var reservationDTO = ReservationRestaurantDTO.fromEntity(reservation);
			var reservationTableDTO = ReservationRestaurantTableDTO.fromEntity(reservation);

			Mockito.when(reservationService.add(ArgumentMatchers.any(ReservationRestaurantDTO.class))).thenReturn(reservationTableDTO);

			System.out.println(reservationDTO);
			System.out.println(asJsonString(reservationDTO));
			//Act
			mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
					.contentType(MediaType.APPLICATION_JSON)
					.content(asJsonString(reservationDTO))
			).andExpect(status().isCreated());

			//Assert
			Mockito.verify(reservationService, Mockito.times(1)).add(ArgumentMatchers.any(ReservationRestaurantDTO.class));
		}
	}
	@Nested
	class updateReservation {
		@Test
		void shouldUpdate() throws Exception {
			//Arrange
			Long id = (long) 1;

			var restaurant = RestaurantUtil.newRestaurant();
			var calendar = CalendarUtil.newCalendar(restaurant);
			restaurant.setCalendar(calendar);

			var table = TableUtil.newTable((long) 1, 1, restaurant);

			var reservation = ReservationUtil.newReservation(id, restaurant);
			reservation.setTables(Set.of(table));

			var reservationDTO = ReservationRestaurantDTO.fromEntity(reservation);
			var reservationTableDTO = ReservationRestaurantTableDTO.fromEntity(reservation);

			Mockito.when(reservationService.update(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(ReservationRestaurantDTO.class))).thenReturn(reservationTableDTO);

			//Act
			mockMvc.perform(MockMvcRequestBuilders.put("/reservations/{id}", id)
							.contentType(MediaType.APPLICATION_JSON)
							.content(asJsonString(reservationDTO)))
					.andExpect(status().isOk());

			System.out.println(mockMvc.toString());
			//Assert
			Mockito.verify(reservationService, Mockito.times(1)).update(ArgumentMatchers.any(Long.class), ArgumentMatchers.any(ReservationRestaurantDTO.class));
		}
	}

	@Nested
	class deleteReservation {
		@Test
		void shouldDelete() throws Exception {
			//Arrange
			Long id = (long)1;
			Mockito.doNothing().when(reservationService).delete(ArgumentMatchers.any(Long.class));

			//Act
			mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/{id}",id))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string("Reservation " + id + " deleted"));

			//Assert
			Mockito.verify(reservationService, Mockito.times(1)).delete(ArgumentMatchers.any(Long.class));
		}
	}
}
