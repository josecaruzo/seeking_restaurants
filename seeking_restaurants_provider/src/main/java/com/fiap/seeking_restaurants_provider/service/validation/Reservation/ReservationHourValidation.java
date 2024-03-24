package com.fiap.seeking_restaurants_provider.service.validation.Reservation;

import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReservationHourValidation implements ConstraintValidator<ValidReservationHour, ReservationRestaurantDTO> {

	@Override
	public boolean isValid(ReservationRestaurantDTO reservationDTO, ConstraintValidatorContext context) {

		/**
		 * All reservation needs to be done in "closed" time, hours finished in 0
		 * Ex.: 17:00 - is valid | 19:00 is valid
		 * Ex.: 17:05 is invalid | 17:30 is invalid
		 */

		if(reservationDTO.reservationHour().getMinute() != 0){
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}
}