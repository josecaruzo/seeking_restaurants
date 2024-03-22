package com.fiap.seeking_restaurants_provider.dto.Calendar;

import com.fiap.seeking_restaurants_provider.entity.Calendar;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import jakarta.validation.constraints.NotBlank;

public record CalendarRestaurantDTO(
		Long id,
		@NotBlank(message = "Preencha uma hora ou defina como 'fechado'") //The monday hour can't be blank
		String monday_hours,
		@NotBlank(message = "Preencha uma hora ou defina como 'fechado'") //The tuesday hour can't be blank
		String tuesday_hours,
		@NotBlank(message = "Preencha uma hora ou defina como 'fechado'") //The wednesday hour can't be blank
		String wednesday_hours,
		@NotBlank(message = "Preencha uma hora ou defina como 'fechado'") //The thursday hour can't be blank
		String thursday_hours,
		@NotBlank(message = "Preencha uma hora ou defina como 'fechado'") //The friday hour can't be blank
		String friday_hours,
		@NotBlank(message = "Preencha uma hora ou defina como 'fechado'") //The saturday hour can't be blank
		String saturday_hours,
		@NotBlank(message = "Preencha uma hora ou defina como 'fechado'") //The sunday hour can't be blank
		String sunday_hours,
		Long restaurant_id
) {

	public static Calendar toEntity(CalendarRestaurantDTO calendarDTO, Restaurant restaurant) {
		return new Calendar(calendarDTO, restaurant);
	}

	public static CalendarRestaurantDTO fromEntity(Calendar calendar) {
		return  new CalendarRestaurantDTO(
				calendar.getId(),
				calendar.getMonday_hours(),
				calendar.getTuesday_hours(),
				calendar.getWednesday_hours(),
				calendar.getThursday_hours(),
				calendar.getFriday_hours(),
				calendar.getSaturday_hours(),
				calendar.getSunday_hours(),
				calendar.getRestaurant() != null ? calendar.getRestaurant().getId() : null
		);
	}

	public static Calendar mapperDtoToEntity(CalendarRestaurantDTO dto, Calendar entity, Restaurant restaurant) {
		entity.setMonday_hours(dto.monday_hours());
		entity.setTuesday_hours(dto.tuesday_hours());
		entity.setWednesday_hours(dto.wednesday_hours());
		entity.setThursday_hours(dto.thursday_hours());
		entity.setFriday_hours(dto.friday_hours());
		entity.setSaturday_hours(dto.saturday_hours());
		entity.setSunday_hours(dto.sunday_hours());
		entity.setRestaurant(restaurant);
		return entity;
	}
}
