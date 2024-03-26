package com.fiap.seeking_restaurants_provider.service.validation.Reservation;

import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantCalendarDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import com.fiap.seeking_restaurants_provider.service.RestaurantService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;

import static java.lang.Integer.parseInt;

public class ReservationRestaurantCalendarValidator implements ConstraintValidator<ValidReservationRestaurantCalendar, ReservationRestaurantDTO> {

	private final RestaurantService restaurantService;

	public ReservationRestaurantCalendarValidator(RestaurantService restaurantService){
		this.restaurantService = restaurantService;
	}
	@Override
	public boolean isValid(ReservationRestaurantDTO reservationDTO, ConstraintValidatorContext context) {
		var dayOfWeek = reservationDTO.reservationDate().getDayOfWeek();
		RestaurantCalendarDTO restaurant = restaurantService.findById(reservationDTO.restaurant_id());

		var restaurantHour = switch (dayOfWeek){
			case MONDAY -> restaurant.calendar().monday_hours();
			case TUESDAY -> restaurant.calendar().tuesday_hours();
			case WEDNESDAY -> restaurant.calendar().wednesday_hours();
			case THURSDAY -> restaurant.calendar().thursday_hours();
			case FRIDAY -> restaurant.calendar().friday_hours();
			case SATURDAY -> restaurant.calendar().saturday_hours();
			case SUNDAY -> restaurant.calendar().sunday_hours();
		};

		var openHour = LocalTime.of(parseInt((restaurantHour.split("-")[0]).substring(0,2)), parseInt((restaurantHour.split("-")[0]).substring(3,5)));
		var closeHour = LocalTime.of(parseInt((restaurantHour.split("-")[1]).substring(0,2)), parseInt((restaurantHour.split("-")[1]).substring(3,5)));
		var reservationHour = reservationDTO.reservationHour();

		// Is valid if reservation hour is equal or after open hour and reservation hour is before close hour
		return (reservationHour.isAfter(openHour) || reservationHour.equals(openHour)) && reservationHour.isBefore(closeHour);

	}
}