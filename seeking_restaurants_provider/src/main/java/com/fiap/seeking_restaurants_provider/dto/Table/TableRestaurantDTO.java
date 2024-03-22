package com.fiap.seeking_restaurants_provider.dto.Table;

import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Table;
import jakarta.validation.constraints.Positive;

public record TableRestaurantDTO(
		Long id,

		@Positive()
		int number,

		@Positive
		int capacity,

		Long restaurant_id
) {
	public static Table toEntity(TableRestaurantDTO tableDTO, Restaurant restaurant) {
		return new Table(tableDTO,restaurant);
	}

	public static TableRestaurantDTO fromEntity(Table table) {
		return new TableRestaurantDTO(
				table.getId(),
				table.getNumber(),
				table.getCapacity(),
				table.getRestaurant() != null ? table.getRestaurant().getId() : null
		);
	}

	public static Table mapperDtoToEntity(TableRestaurantDTO dto, Table entity, Restaurant restaurant) {
		entity.setNumber(dto.number());
		entity.setCapacity(dto.capacity());
		entity.setRestaurant(restaurant);
		return entity;
	}
}
