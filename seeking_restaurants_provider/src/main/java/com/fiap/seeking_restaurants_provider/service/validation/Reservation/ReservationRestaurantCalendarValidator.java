package com.fiap.seeking_restaurants_provider.service.validation.Reservation;

import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;

import static java.lang.Integer.parseInt;

public class ReservationRestaurantCalendarValidator implements ConstraintValidator<ValidReservationRestaurantCalendar, ReservationRestaurantDTO> {
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Override
	public boolean isValid(ReservationRestaurantDTO reservationDTO, ConstraintValidatorContext context) {
		var dayOfWeek = reservationDTO.reservationDate().getDayOfWeek();
		Restaurant restaurant = restaurantRepository.getReferenceById(reservationDTO.restaurant_id());

		var restaurantHour = switch (dayOfWeek){
			case MONDAY -> restaurant.getCalendar().getMonday_hours();
			case TUESDAY -> restaurant.getCalendar().getTuesday_hours();
			case WEDNESDAY -> restaurant.getCalendar().getWednesday_hours();
			case THURSDAY -> restaurant.getCalendar().getThursday_hours();
			case FRIDAY -> restaurant.getCalendar().getFriday_hours();
			case SATURDAY -> restaurant.getCalendar().getSaturday_hours();
			case SUNDAY -> restaurant.getCalendar().getSunday_hours();
		};

		var openHour = LocalTime.of(parseInt((restaurantHour.split("-")[0]).substring(0,2)), parseInt((restaurantHour.split("-")[0]).substring(3,5)));
		var closeHour = LocalTime.of(parseInt((restaurantHour.split("-")[1]).substring(0,2)), parseInt((restaurantHour.split("-")[1]).substring(3,5)));
		var reservationHour = reservationDTO.reservationHour();

		// Is valid if reservation hour is equal or after open hour and reservation hour is before close hour
		return (reservationHour.isAfter(openHour) || reservationHour.equals(openHour)) && reservationHour.isBefore(closeHour);

	}
}