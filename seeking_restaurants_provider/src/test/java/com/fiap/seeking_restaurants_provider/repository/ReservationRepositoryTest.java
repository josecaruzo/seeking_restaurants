package com.fiap.seeking_restaurants_provider.repository;

import com.fiap.seeking_restaurants_provider.entity.Reservation;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.utils.ReservationUtil;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import com.fiap.seeking_restaurants_provider.utils.ReviewUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReservationRepositoryTest {
	@Mock
	private ReservationRepository reservationRepository;

	AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}

	@Test
	void shouldFindReservation(){
		//Arrange
		Long id = (long) 1;
		var reservation = ReservationUtil.newReservation(id);
		Mockito.when(reservationRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(reservation));

		//Act
		var optReservation = reservationRepository.findById(id);

		//Assert
		Mockito.verify(reservationRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
		Assertions.assertThat(optReservation)
				.isPresent()
				.containsSame(reservation);
	}

	@Test
	void shouldFindAllReservations(){
		//Arrange
		Long id = null;
		id = (long) 1;
		var reservation1 = ReservationUtil.newReservation(id);
		id = (long) 2;
		var reservation2 = ReservationUtil.newReservation(id);

		var reservationList = Arrays.asList(reservation1, reservation2);

		Mockito.when(reservationRepository.findAll()).thenReturn(reservationList);

		//Act
		var reservations = reservationRepository.findAll();

		//Assert
		Mockito.verify(reservationRepository, Mockito.times(1)).findAll();
		Assertions.assertThat(reservations)
				.hasSize(2)
				.containsExactlyInAnyOrder(reservation1,reservation2);
	}
	@Test
	void shouldFindByRestaurant() {
		//Arrange
		Long id = null;
		var restaurant = RestaurantUtil.newRestaurant();
		id = (long) 1;
		var reservation1 = ReservationUtil.newReservation(id, restaurant);
		id = (long) 2;
		var reservation2 = ReservationUtil.newReservation(id, restaurant);

		var reservationList = Arrays.asList(reservation1, reservation2);

		Mockito.when(reservationRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(reservationList);

		//Act
		var reviews = reservationRepository.findByRestaurant(restaurant);

		//Assert
		Mockito.verify(reservationRepository, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));
		Assertions.assertThat(reviews)
				.hasSize(2)
				.containsExactlyInAnyOrder(reservation1,reservation2);
	}

	@Test
	void shouldFindByRestaurantAndReservationDate() {
		//Arrange
		Long id = null;
		var restaurant = RestaurantUtil.newRestaurant();
		id = (long) 1;
		var reservation1 = ReservationUtil.newReservation(id, restaurant);
		id = (long) 2;
		var reservation2 = ReservationUtil.newReservation(id, restaurant);

		var reservationList = Arrays.asList(reservation1, reservation2);

		Mockito.when(reservationRepository.findByRestaurantAndReservationDate(
				ArgumentMatchers.any(Restaurant.class),
				ArgumentMatchers.any(LocalDate.class))).thenReturn(reservationList);

		//Act
		var reviews = reservationRepository.findByRestaurantAndReservationDate(restaurant, reservation1.getReservationDate());

		//Assert
		Mockito.verify(reservationRepository, Mockito.times(1)).findByRestaurantAndReservationDate(
				ArgumentMatchers.any(Restaurant.class),
				ArgumentMatchers.any(LocalDate.class)
		);
		Assertions.assertThat(reviews)
				.hasSize(2)
				.containsExactlyInAnyOrder(reservation1,reservation2);
	}

	@Test
	void shouldFindByRestaurantAndReservationDateAndReservationHour() {
		//Arrange
		Long id = null;
		var restaurant = RestaurantUtil.newRestaurant();
		id = (long) 1;
		var reservation1 = ReservationUtil.newReservation(id, restaurant);
		id = (long) 2;
		var reservation2 = ReservationUtil.newReservation(id, restaurant);

		var reservationList = Arrays.asList(reservation1, reservation2);

		Mockito.when(reservationRepository.findByRestaurantAndReservationDateAndReservationHour(
				ArgumentMatchers.any(Restaurant.class),
				ArgumentMatchers.any(LocalDate.class),
				ArgumentMatchers.any(LocalTime.class))).thenReturn(reservationList);

		//Act
		var reviews = reservationRepository.findByRestaurantAndReservationDateAndReservationHour(
				restaurant,
				reservation1.getReservationDate(),
				reservation1.getReservationHour());

		//Assert
		Mockito.verify(reservationRepository, Mockito.times(1)).findByRestaurantAndReservationDateAndReservationHour(
				ArgumentMatchers.any(Restaurant.class),
				ArgumentMatchers.any(LocalDate.class),
				ArgumentMatchers.any(LocalTime.class)
		);
		Assertions.assertThat(reviews)
				.hasSize(2)
				.containsExactlyInAnyOrder(reservation1,reservation2);
	}

	@Test
	void shouldSaveReservation() {
		//Arrange
		var reservation = ReservationUtil.newReservation();
		Mockito.when(reservationRepository.save(ArgumentMatchers.any(Reservation.class))).thenReturn(reservation);

		//Act
		var newReservation = reservationRepository.save(reservation);

		//Assert
		Mockito.verify(reservationRepository, Mockito.times(1)).save(ArgumentMatchers.any(Reservation.class));
		//NewReservation is not null
		Assertions.assertThat(newReservation)
				.isInstanceOf(Reservation.class)
				.isNotNull()
				.isEqualTo(reservation);
		//New reservation id is not null, so successfully created auto increment id
		Assertions.assertThat(newReservation)
				.extracting(Reservation::getId)
				.isNotNull();
	}

	@Test
	void shouldDeleteReservation(){
		// Arrange
		Long id = (long) 1;
		Mockito.doNothing().when(reservationRepository).deleteById(ArgumentMatchers.any(Long.class));
		// Act
		reservationRepository.deleteById(id);
		var findReservation = reservationRepository.findById(id);
		// Assert
		Mockito.verify(reservationRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));
		Mockito.verify(reservationRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
		Assertions.assertThat(findReservation).isNotPresent();
	}
}
