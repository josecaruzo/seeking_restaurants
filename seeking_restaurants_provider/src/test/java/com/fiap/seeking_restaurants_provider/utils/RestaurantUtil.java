package com.fiap.seeking_restaurants_provider.utils;

import com.fiap.seeking_restaurants_provider.entity.Calendar;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;

public class RestaurantUtil {
	public static Restaurant newRestaurant(){
		return new Restaurant(
				(long)1,
				"Restaurant test",
				"11111-999",
				"Address test",
				"Type test",
				10
		);
	}

	public static Restaurant newRestaurant(Long id){
		return new Restaurant(
				id,
				"Restaurant test",
				"11111-999",
				"Address test",
				"Type test",
				10
		);
	}

	public static Restaurant newRestaurant(Long id, Calendar calendar){
		var restaurant = new Restaurant(
				id,
				"Restaurant test",
				"11111-999",
				"Address test",
				"Type test",
				10
		);
		restaurant.setCalendar(calendar);

		return restaurant;
	}

	public static Restaurant newRestaurantByName(Long id, String name){
		return new Restaurant(
				id,
				name,
				"11111-999",
				"Address test",
				"Type test",
				10
		);
	}

	public static Restaurant newRestaurantByType(Long id, String type){
		return new Restaurant(
				id,
				"Restaurant test",
				"11111-999",
				"Address test",
				type,
				10
		);
	}

}
