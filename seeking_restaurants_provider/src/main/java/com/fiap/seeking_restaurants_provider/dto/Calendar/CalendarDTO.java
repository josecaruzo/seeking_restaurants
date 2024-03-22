package com.fiap.seeking_restaurants_provider.dto.Calendar;

import com.fiap.seeking_restaurants_provider.entity.Calendar;
import jakarta.validation.constraints.NotBlank;

public record CalendarDTO(
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
		String sunday_hours
) {

	public static Calendar toEntity(CalendarDTO calendarDTO) {
		return new Calendar(calendarDTO);
	}

	public static CalendarDTO fromEntity(Calendar calendar) {
		return  new CalendarDTO(
				calendar.getId(),
				calendar.getMonday_hours(),
				calendar.getTuesday_hours(),
				calendar.getWednesday_hours(),
				calendar.getThursday_hours(),
				calendar.getFriday_hours(),
				calendar.getSaturday_hours(),
				calendar.getSunday_hours()
		);
	}

	public static Calendar mapperDtoToEntity(CalendarDTO dto, Calendar entity) {
		entity.setMonday_hours(dto.monday_hours());
		entity.setTuesday_hours(dto.tuesday_hours());
		entity.setWednesday_hours(dto.wednesday_hours());
		entity.setThursday_hours(dto.thursday_hours());
		entity.setFriday_hours(dto.friday_hours());
		entity.setSaturday_hours(dto.saturday_hours());
		entity.setSunday_hours(dto.sunday_hours());
		return entity;
	}
}
