package com.fiap.seeking_restaurants_provider.repository;

import com.fiap.seeking_restaurants_provider.entity.Reservation;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByRestaurant(Restaurant restaurant);
	List<Reservation> findByRestaurantAndReservationDate(Restaurant restaurant, LocalDate reservationDate);
	List<Reservation> findByRestaurantAndReservationDateAndReservationHour(Restaurant restaurant, LocalDate reservationDate, LocalTime reservationHour);

}
