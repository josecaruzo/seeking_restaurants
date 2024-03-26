package com.fiap.seeking_restaurants_provider.dto.Restaurant;

import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.service.validation.Restaurant.ValidRestaurantCreation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

//@ValidRestaurantCreation(message = "Já foi cadastrado um restaurante com esse nome")
public record RestaurantDTO(
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
		int capacity
) {
	public static Restaurant toEntity(RestaurantDTO restaurantDTO) {
		return new Restaurant(restaurantDTO);
	}

	public static RestaurantDTO fromEntity(Restaurant restaurant) {
		return  new RestaurantDTO(
				restaurant.getId(),
				restaurant.getName(),
				restaurant.getZipcode(),
				restaurant.getAddress(),
				restaurant.getType(),
				restaurant.getCapacity()
		);
	}

	public static RestaurantDTO fromDTO(RestaurantCalendarDTO restaurantCalendarDTO) {
		return  new RestaurantDTO(
				restaurantCalendarDTO.id(),
				restaurantCalendarDTO.name(),
				restaurantCalendarDTO.zipcode(),
				restaurantCalendarDTO.address(),
				restaurantCalendarDTO.type(),
				restaurantCalendarDTO.capacity()
		);
	}

	public static Restaurant mapperDtoToEntity(RestaurantDTO dto, Restaurant entity) {
		entity.setName(dto.name());
		entity.setZipcode(dto.zipcode());
		entity.setAddress(dto.address());
		entity.setType(dto.type());
		entity.setCapacity(dto.capacity());
		return entity;
	}
}
