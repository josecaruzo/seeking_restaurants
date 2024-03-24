package com.fiap.seeking_restaurants_provider.dto.Review;

import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewRestaurantDTO(
		Long id,

		@NotBlank(message = "O nome do cliente não pode ser vazio") // THe guest name can't be blank
		String guestName,

		@Min(value = 1, message = "Não pode dar menos que 1 estrela") // Review can't have less than 1 star
		@Max(value = 5, message = "Não pode dar mais que 5 estrelas") // Review can't have more than 5 stars
		int stars, // number between 1 and 5

		String comment, //review comment is optional
		Long restaurant_id
) {

	public static Review toEntity(ReviewRestaurantDTO reviewDTO, Restaurant restaurant) {
		return new Review(reviewDTO, restaurant);
	}

	public static ReviewRestaurantDTO fromEntity(Review review) {
		return  new ReviewRestaurantDTO(
				review.getId(),
				review.getGuestName(),
				review.getStars(),
				review.getComment(),
				review.getRestaurant() != null ? review.getRestaurant().getId() : null
		);
	}

	public static Review mapperDtoToEntity(ReviewRestaurantDTO dto, Review entity, Restaurant restaurant) {
		entity.setGuestName(dto.guestName());
		entity.setStars(dto.stars());
		entity.setComment(dto.comment());
		entity.setRestaurant(restaurant);
		return entity;
	}
}
