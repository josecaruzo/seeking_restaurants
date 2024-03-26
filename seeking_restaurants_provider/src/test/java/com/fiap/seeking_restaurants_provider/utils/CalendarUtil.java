package com.fiap.seeking_restaurants_provider.utils;

import com.fiap.seeking_restaurants_provider.entity.Calendar;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;

public abstract class CalendarUtil {
	public static Calendar newCalendar(){
		return new Calendar(
				(long) 1,
				"12:00-22:00",
				"12:00-22:00",
				"12:00-22:00",
				"12:00-22:00",
				"12:00-22:00",
				"12:00-23:00",
				"12:00-16:00"
		);

	}

	public static Calendar newCalendar(Long id){
		return new Calendar(
				id,
				"12:00-22:00",
				"12:00-22:00",
				"12:00-22:00",
				"12:00-22:00",
				"12:00-22:00",
				"12:00-23:00",
				"12:00-16:00"
		);
	}

	public static Calendar newCalendar(Restaurant restaurant){
		Calendar calendar = new Calendar(
				(long) 1,
				"12:00-22:00",
				"12:00-22:00",
				"12:00-22:00",
				"12:00-22:00",
				"12:00-22:00",
				"12:00-23:00",
				"12:00-16:00"
		);

		calendar.setRestaurant(restaurant);
		return calendar;
	}
}
