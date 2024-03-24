package com.fiap.seeking_restaurants_provider.service.validation.Reservation;

import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class ReservationCreationValidator implements ConstraintValidator<ValidReservationCreation, ReservationRestaurantDTO> {

	@Override
	public boolean isValid(ReservationRestaurantDTO reservationDTO, ConstraintValidatorContext context) {
		LocalDateTime reservationDateTime = LocalDateTime.of(reservationDTO.reservationDate(), reservationDTO.reservationHour());

		return reservationDateTime.isAfter(LocalDateTime.now());
	}
}
