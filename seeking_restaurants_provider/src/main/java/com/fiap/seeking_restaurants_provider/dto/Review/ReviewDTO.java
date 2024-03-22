package com.fiap.seeking_restaurants_provider.dto.Review;

import com.fiap.seeking_restaurants_provider.entity.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewDTO (
		Long id,

		@NotBlank
		String guestName,

		@Min(value = 1, message = "Não pode dar menos que 1 estrela") // Review can't have less than 1 star
		@Max(value = 5, message = "Não pode dar mais que 5 estrelas") // Review can't have more than 5 stars
		int stars, // number between 1 and 5

		String comment //review comment is optional

){

	public static Review toEntity(ReviewDTO reviewDTO) {
		return new Review(reviewDTO);
	}

	public static ReviewDTO fromEntity(Review review) {
		return  new ReviewDTO(
				review.getId(),
				review.getGuestName(),
				review.getStars(),
				review.getComment()
		);
	}

	public static Review mapperDtoToEntity(ReviewDTO dto, Review entity) {
		entity.setGuestName(dto.guestName());
		entity.setStars(dto.stars());
		entity.setComment(dto.comment());
		return entity;
	}
}
