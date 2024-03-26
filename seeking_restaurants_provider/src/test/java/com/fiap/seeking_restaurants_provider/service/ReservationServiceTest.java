package com.fiap.seeking_restaurants_provider.service;


import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantTableDTO;
import com.fiap.seeking_restaurants_provider.entity.*;
import com.fiap.seeking_restaurants_provider.repository.ReservationRepository;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import com.fiap.seeking_restaurants_provider.repository.TableRepository;
import com.fiap.seeking_restaurants_provider.utils.ReservationUtil;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import com.fiap.seeking_restaurants_provider.utils.TableUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

public class ReservationServiceTest {
	private ReservationService reservationService;

	@Mock
	private ReservationRepository reservationRepository;
	@Mock
	private RestaurantRepository restaurantRepository;
	@Mock
	private TableRepository tableRepository;
	AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		reservationService = new ReservationService(reservationRepository, restaurantRepository, tableRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}

	@Nested
	class findReservation {
		@Test
		void shouldFindByRestaurant() {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var reservation1 = ReservationUtil.newReservation((long)1, restaurant);
			var reservation2 = ReservationUtil.newReservation((long)2, restaurant);

			var reservationList = Arrays.asList(reservation1, reservation2);
			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);
			Mockito.when(reservationRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(reservationList);

			//Act
			var findReservations = reservationService.findByRestaurant(restaurant.getId());

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(reservationRepository, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));

			Assertions.assertThat(findReservations)
					.hasSize(2)
					.containsExactlyInAnyOrder(ReservationRestaurantTableDTO.fromEntity(reservation1), ReservationRestaurantTableDTO.fromEntity(reservation2));
		}

		@Test
		void shouldFindByRestaurantAndReservationDate() {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var reservation1 = ReservationUtil.newReservation((long)1, restaurant);
			var reservation2 = ReservationUtil.newReservation((long)2, restaurant);
			var reservationDate = reservation1.getReservationDate();

			var reservationList = Arrays.asList(reservation1, reservation2);
			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);
			Mockito.when(reservationRepository.findByRestaurantAndReservationDate(
					ArgumentMatchers.any(Restaurant.class),
					ArgumentMatchers.any(LocalDate.class))
			).thenReturn(reservationList);

			//Act
			var findReservations = reservationService.findByRestaurantAndReservationDate(restaurant.getId(),reservationDate);

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(reservationRepository, Mockito.times(1)).findByRestaurantAndReservationDate(
					ArgumentMatchers.any(Restaurant.class),
					ArgumentMatchers.any(LocalDate.class));

			Assertions.assertThat(findReservations)
					.hasSize(2)
					.containsExactlyInAnyOrder(ReservationRestaurantTableDTO.fromEntity(reservation1), ReservationRestaurantTableDTO.fromEntity(reservation2));
		}

		@Test
		void shouldFindByRestaurantAndReservationDateAndReservationHour() {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var reservation1 = ReservationUtil.newReservation((long)1, restaurant);
			var reservation2 = ReservationUtil.newReservation((long)2, restaurant);

			var reservationDate = reservation1.getReservationDate();
			var reservationHour = reservation1.getReservationHour();

			var reservationList = Arrays.asList(reservation1, reservation2);
			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);
			Mockito.when(reservationRepository.findByRestaurantAndReservationDateAndReservationHour(
					ArgumentMatchers.any(Restaurant.class),
					ArgumentMatchers.any(LocalDate.class),
					ArgumentMatchers.any(LocalTime.class))
			).thenReturn(reservationList);

			//Act
			var findReservations = reservationService.findByRestaurantAndReservationDateAndReservationHour(
					restaurant.getId(),
					reservationDate,
					reservationHour
			);

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(reservationRepository, Mockito.times(1)).findByRestaurantAndReservationDateAndReservationHour(
					ArgumentMatchers.any(Restaurant.class),
					ArgumentMatchers.any(LocalDate.class),
					ArgumentMatchers.any(LocalTime.class));
			Assertions.assertThat(findReservations)
					.hasSize(2)
					.containsExactlyInAnyOrder(ReservationRestaurantTableDTO.fromEntity(reservation1), ReservationRestaurantTableDTO.fromEntity(reservation2));
		}
	}
	@Nested
	class addReservation {
		@Test
		void shouldAdd() {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var reservation = ReservationUtil.newReservation((long)1, restaurant);

			var reservationDTO = ReservationRestaurantDTO.fromEntity(reservation);
			var tables = TableUtil.newTables(restaurant);

			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);
			Mockito.when(tableRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(tables);
			Mockito.when(reservationRepository.save(ArgumentMatchers.any(Reservation.class))).thenAnswer(i -> i.getArgument(0));
			Mockito.when(reservationRepository.findByRestaurantAndReservationDateAndReservationHour(
					ArgumentMatchers.any(Restaurant.class),
					ArgumentMatchers.any(LocalDate.class),
					ArgumentMatchers.any(LocalTime.class))
			).thenReturn(Collections.emptyList());

			//Act
			var newReservation = reservationService.add(reservationDTO);

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(tableRepository, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));
			Mockito.verify(reservationRepository, Mockito.times(1)).save(ArgumentMatchers.any(Reservation.class));
			Mockito.verify(reservationRepository, Mockito.times(1)).findByRestaurantAndReservationDateAndReservationHour(
					ArgumentMatchers.any(Restaurant.class),
					ArgumentMatchers.any(LocalDate.class),
					ArgumentMatchers.any(LocalTime.class)
			);

			Assertions.assertThat(newReservation)
					.isInstanceOf(ReservationRestaurantTableDTO.class)
					.isNotNull();

			Assertions.assertThat(newReservation.id()).isNotNull();
		}
	}

	@Nested
	class updateReservation {
		@Test
		void shouldUpdate() {
			//Arrange
			Long id = (long)1;
			var restaurant = RestaurantUtil.newRestaurant();
			var oldReservation = ReservationUtil.newReservation(id, restaurant);

			var tables = TableUtil.newTables(restaurant);

			var newReservation = new Reservation(ReservationRestaurantDTO.fromEntity(oldReservation), restaurant);
			newReservation.setTotalTables(newReservation.getTotalTables() + 1); //trying to schedule 1 more table

			Mockito.when(reservationRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(newReservation);
			Mockito.when(tableRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(tables);
			Mockito.when(reservationRepository.save(ArgumentMatchers.any(Reservation.class))).thenAnswer(i -> i.getArgument(0));
			Mockito.when(reservationRepository.findByRestaurantAndReservationDateAndReservationHour(
					ArgumentMatchers.any(Restaurant.class),
					ArgumentMatchers.any(LocalDate.class),
					ArgumentMatchers.any(LocalTime.class))
			).thenReturn(Collections.emptyList());

			//Act
			var updatedReservation = reservationService.update(id, ReservationRestaurantDTO.fromEntity(newReservation));

			//Assert
			Mockito.verify(reservationRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(tableRepository, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));
			Mockito.verify(reservationRepository, Mockito.times(1)).save(ArgumentMatchers.any(Reservation.class));
			Mockito.verify(reservationRepository, Mockito.times(1)).findByRestaurantAndReservationDateAndReservationHour(
					ArgumentMatchers.any(Restaurant.class),
					ArgumentMatchers.any(LocalDate.class),
					ArgumentMatchers.any(LocalTime.class)
			);

			Assertions.assertThat(updatedReservation)
					.isInstanceOf(ReservationRestaurantTableDTO.class)
					.isNotNull()
					.isNotEqualTo(ReservationRestaurantTableDTO.fromEntity(oldReservation));

			Assertions.assertThat(updatedReservation.id()).isNotNull();
		}
	}

	@Nested
	class deleteReservation {
		@Test
		void shouldDelete() {
			// Arrange
			Long id = (long) 1;
			var restaurant = RestaurantUtil.newRestaurant();
			var reservation = ReservationUtil.newReservation(id, restaurant);
			Mockito.doNothing().when(reservationRepository).deleteById(ArgumentMatchers.any(Long.class));

			// Act
			reservationService.delete(reservation.getId());

			// Assert
			Mockito.verify(reservationRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));

		}
	}
}
