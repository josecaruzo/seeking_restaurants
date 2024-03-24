package com.fiap.seeking_restaurants_provider.service.validation.Calendar;

import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class CalendarCreationValidator implements ConstraintValidator <ValidCalendarCreation,CalendarDTO> {
	public Boolean dateValidation(String day){
		/**
		 * Valid format hh:mm-hh:mm , considering:
		 * hh from 0 to 23
		 * mm from 0 to 59
		 */

		if(day.equalsIgnoreCase("fechado") || day.equalsIgnoreCase("closed")){
			return Boolean.TRUE;
		}

		Pattern regex = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]-(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$"); // hh:mm-hh:mm (24h format)
		Matcher matcher = regex.matcher(day);

		return matcher.matches();


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
