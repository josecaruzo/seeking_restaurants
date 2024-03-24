package com.fiap.seeking_restaurants_provider.service.validation.Reservation;

import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReservationPhoneNumberValidator implements ConstraintValidator<ValidReservationPhoneNumber, ReservationRestaurantDTO> {

	@Override
	public boolean isValid(ReservationRestaurantDTO reservationDTO, ConstraintValidatorContext context) {

		if(reservationDTO.guestPhone().length() > 15 || reservationDTO.guestPhone().length() < 13){
			return Boolean.FALSE;
		}

		Pattern regex = Pattern.compile("^\\([0-9]{2}\\)[0-9]?[0-9]{4}-[0-9]{4}$"); // (99)99999-9999 or (99)9999-9999
		Matcher matcher = regex.matcher(reservationDTO.guestPhone());


		return matcher.matches();
	}
}