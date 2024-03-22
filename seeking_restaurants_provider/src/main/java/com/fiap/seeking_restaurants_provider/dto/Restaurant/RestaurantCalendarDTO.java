package com.fiap.seeking_restaurants_provider.dto.Restaurant;

import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Calendar;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RestaurantCalendarDTO(
		Long id,
		@NotBlank(message = "O nome não pode ser vazio") //The name can't be blank
		String name,
		@NotBlank(message = "O cep não pode ser vazio") // The zipcode can't be blank
		String zipcode,

		@NotBlank(message = "O endereço não pode ser vazio") // The address can't be blank
		String address,

		@NotBlank(message = "O tipo não pode ser vazio") // The type can't be blank
		String type,

		@Min(value = 0, message = "A capacidade não pode ser menor ou igual a zero") // The capacity can't be negative or zero
		int capacity,

		CalendarDTO calendar
) {
	public static Restaurant toEntity(RestaurantCalendarDTO restaurantDTO) {
		return new Restaurant(restaurantDTO);
	}

	public static RestaurantCalendarDTO fromEntity(Restaurant restaurant) {
		return  new RestaurantCalendarDTO(
				restaurant.getId(),
				restaurant.getName(),
				restaurant.getZipcode(),
				restaurant.getAddress(),
				restaurant.getType(),
				restaurant.getCapacity(),
				restaurant.getCalendar() != null ? CalendarDTO.fromEntity(restaurant.getCalendar()) : null
		);
	}

	public static Restaurant mapperDtoToEntity(RestaurantCalendarDTO dto, Restaurant entity, Calendar calendar) {
		entity.setName(dto.name());
		entity.setZipcode(dto.zipcode());
		entity.setAddress(dto.address());
		entity.setType(dto.type());
		entity.setCapacity(dto.capacity());
		entity.setCalendar(calendar);
		return entity;
	}
}

