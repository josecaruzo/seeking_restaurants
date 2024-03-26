package com.fiap.seeking_restaurants_provider.utils;

import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TableUtil {

	public static Table newTable(){

		return new Table(
				(long)1,
				1,
				4);
	}

	public static Table newTable(Long id){
		return new Table(
				id,
				1,
				4);
	}

	public static Table newTable(Long id, int number, Restaurant restaurant){
		Table table =  new Table(id,number,4);
		table.setRestaurant(restaurant);
		return table;
	}

	public static List<Table> newTables(Restaurant restaurant){
		List<Table> newTables = new ArrayList<>();

		for (int i = 1; i <= restaurant.getCapacity(); i++) {
			Table table = new Table();
			//table.setId((long)i);
			table.setNumber(i);
			table.setCapacity(4); // default capacity as 4
			table.setRestaurant(restaurant);

			newTables.add(table);
		}

		return newTables;
	}
}
