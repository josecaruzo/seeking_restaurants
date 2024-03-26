package com.fiap.seeking_restaurants_provider.utils;

import com.fiap.seeking_restaurants_provider.entity.Reservation;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationUtil {
	public static Reservation newReservation(){
		return new Reservation(
				(long) 1,
				LocalDate.now(),
				LocalTime.now().plusHours(1).withNano(0).withSecond(0).withMinute(0),
				"Client test",
				"email@test.com",
				"(99)98888-8888",
				1
		);
	}

	public static Reservation newReservation(Long id){
		return new Reservation(
				id,
				LocalDate.now(),
				LocalTime.now().plusHours(1).withNano(0).withSecond(0).withMinute(0),
				"Client test",
				"email@test.com",
				"(99)98888-8888",
				1
		);
	}

	public static Reservation newReservation(Long id, Restaurant restaurant){
		var reservation = new Reservation(
				id,
				LocalDate.now(),
				LocalTime.now().plusHours(1).withNano(0).withSecond(0).withMinute(0),
				"Client test",
				"email@test.com",
				"(99)98888-8888",
				1
		);

		reservation.setRestaurant(restaurant);
		return reservation;
	}

}
