package com.fiap.seeking_restaurants_provider.service;

import com.fiap.seeking_restaurants_provider.controller.exception.DatabaseException;
import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarRestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Calendar;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.repository.CalendarRepository;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {
	private final CalendarRepository calendarRepository;
	private final RestaurantRepository restaurantRepository;

	public Boolean dateValidation(String day){
		/**
		 * Valid format hh:mm-hh:mm , considering:
		 * hh from 0 to 24
		 * mm from 0 to 60
		 */

		if(day.equalsIgnoreCase("fechado") || day.equalsIgnoreCase("closed")){
			return Boolean.TRUE;
		}
		else{
			var hours = day.split("-");
			if(hours.length == 2) {
				var open_time = hours[0].split(":");
				var close_time = hours[1].split(":");

				if (open_time.length == 2 && close_time.length == 2) {
					if ((Integer.parseInt(open_time[0]) >= 0 && Integer.parseInt(open_time[0]) <= 24) &&
							(Integer.parseInt(close_time[0]) >= 0 && Integer.parseInt(close_time[0]) <= 24) &&
							(Integer.parseInt(open_time[1]) >= 0 && Integer.parseInt(open_time[1]) <= 60) &&
							(Integer.parseInt(close_time[1]) >= 0 && Integer.parseInt(close_time[1]) <= 60)) {
						return Boolean.TRUE;
					}
				}
			}
		}
		return Boolean.FALSE;
	}

	@Autowired
	public CalendarService(CalendarRepository calendarRepository, RestaurantRepository restaurantRepository){
		this.calendarRepository = calendarRepository;
		this.restaurantRepository = restaurantRepository;
	}

	public CalendarDTO findById(Long id){
		try{
			Calendar calendar = calendarRepository.getReferenceById(id);
			return CalendarDTO.fromEntity(calendar);
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Calendário não encontrado"); // Calendar not found
		}
	}

	public CalendarRestaurantDTO add(CalendarRestaurantDTO dto) {
		try{
			Boolean validDate = Boolean.TRUE;
			Restaurant restaurant = restaurantRepository.getReferenceById(dto.restaurant_id());
			Calendar calendar = CalendarRestaurantDTO.toEntity(dto, restaurant);

			var newCalendar = calendarRepository.save(calendar);

			return CalendarRestaurantDTO.fromEntity(newCalendar);
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Restaurante não encontrado"); // Restaurant not found
		}
	}

	public CalendarDTO update(Long id, CalendarDTO calendarDTO){
		try{
			Calendar updateCalendar = calendarRepository.getReferenceById(id);

			updateCalendar.setMonday_hours(calendarDTO.monday_hours());
			updateCalendar.setTuesday_hours(calendarDTO.tuesday_hours());
			updateCalendar.setWednesday_hours(calendarDTO.wednesday_hours());
			updateCalendar.setThursday_hours(calendarDTO.thursday_hours());
			updateCalendar.setFriday_hours(calendarDTO.friday_hours());
			updateCalendar.setSaturday_hours(calendarDTO.saturday_hours());
			updateCalendar.setSunday_hours(calendarDTO.sunday_hours());

			updateCalendar = calendarRepository.save(updateCalendar);
			return CalendarDTO.fromEntity(updateCalendar);
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Restaurante não encontrado"); // Restaurant not found
		}
	}

}
