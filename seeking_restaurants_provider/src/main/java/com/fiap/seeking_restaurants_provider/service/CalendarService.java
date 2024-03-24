package com.fiap.seeking_restaurants_provider.service;

import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarRestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Calendar;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.repository.CalendarRepository;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {
	private final CalendarRepository calendarRepository;
	private final RestaurantRepository restaurantRepository;

	@Autowired
	public CalendarService(CalendarRepository calendarRepository, RestaurantRepository restaurantRepository){
		this.calendarRepository = calendarRepository;
		this.restaurantRepository = restaurantRepository;
	}

	public CalendarRestaurantDTO add(CalendarRestaurantDTO dto) {
		try{
			Restaurant restaurant = restaurantRepository.getReferenceById(dto.restaurant_id());
			Calendar calendar = CalendarRestaurantDTO.toEntity(dto, restaurant);

			var newCalendar = calendarRepository.save(calendar);

			return CalendarRestaurantDTO.fromEntity(newCalendar);
		}catch (DataIntegrityViolationException | LazyInitializationException e ) {
			throw new EntityNotFoundException("Restaurante não encontrado"); // Restaurant not found
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
		}catch ( DataIntegrityViolationException | LazyInitializationException e ) {
			throw new EntityNotFoundException("Calendário não encontrado"); // Calendar not found
		}
	}

}
