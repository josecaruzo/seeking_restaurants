package com.fiap.seeking_restaurants_provider.service.validation.Calendar;

import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class CalendarOpenAndCloseHourValidator  implements ConstraintValidator<ValidOpenAndCloseHour,CalendarDTO> {
	public Boolean dateValidation(String day){
		if(day.equalsIgnoreCase("fechado") || day.equalsIgnoreCase("closed")){
			return Boolean.TRUE;
		}

		Pattern regex = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]-(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"); // hh:mm-hh:mm (24h format)
		Matcher matcher = regex.matcher(day);

		if (matcher.matches()){
			var hours = day.split("-");
			var openHour = LocalTime.of(parseInt(hours[0].substring(0, 2)), parseInt(hours[0].substring(3, 5)));
			var closeHour = LocalTime.of(parseInt(hours[1].substring(0, 2)), parseInt(hours[1].substring(3, 5)));

			//If dateTime format matches the regex (CalendarCreationValidator), validates if the open hour is before close hour
			return openHour.isBefore(closeHour);
		}
		return Boolean.FALSE;
	}

	@Override
	public boolean isValid(CalendarDTO calendarDTO, ConstraintValidatorContext context){
		Boolean validMonday = dateValidation(calendarDTO.monday_hours());
		Boolean validTuesday = dateValidation(calendarDTO.tuesday_hours());
		Boolean validWednesday = dateValidation(calendarDTO.wednesday_hours());
		Boolean validThursday = dateValidation(calendarDTO.thursday_hours());
		Boolean validFriday = dateValidation(calendarDTO.friday_hours());
		Boolean validSaturday = dateValidation(calendarDTO.saturday_hours());
		Boolean validSunday = dateValidation(calendarDTO.sunday_hours());

		return (validMonday && validTuesday && validWednesday && validThursday && validFriday && validSaturday && validSunday);
	}
}
