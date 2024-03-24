package com.fiap.seeking_restaurants_provider.service.validation.Restaurant;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantCalendarDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class RestaurantCalendarCreationValidator implements ConstraintValidator<ValidRestaurantCreation, RestaurantCalendarDTO> {
	@Autowired
	private RestaurantRepository restaurantRepository;

	@Override
	public boolean isValid(RestaurantCalendarDTO restaurantCalendarDTO, ConstraintValidatorContext context) {
		Optional<Restaurant> restaurant = restaurantRepository.findByName(restaurantCalendarDTO.name());

		return restaurant.isEmpty();
	}
}

