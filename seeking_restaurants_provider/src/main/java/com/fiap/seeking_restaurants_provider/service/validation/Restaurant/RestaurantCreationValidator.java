package com.fiap.seeking_restaurants_provider.service.validation.Restaurant;

import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class RestaurantCreationValidator implements ConstraintValidator<ValidRestaurantCreation, RestaurantDTO> {
	private final RestaurantRepository restaurantRepository;

	public RestaurantCreationValidator(RestaurantRepository restaurantRepository){
		this.restaurantRepository = restaurantRepository;
	}

	@Override
	public boolean isValid(RestaurantDTO restaurantDTO, ConstraintValidatorContext context) {
		Optional<Restaurant> restaurant = restaurantRepository.findByName(restaurantDTO.name());
		return restaurant.isEmpty();
	}
}
