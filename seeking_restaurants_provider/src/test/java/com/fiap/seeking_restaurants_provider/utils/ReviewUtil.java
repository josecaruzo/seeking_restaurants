package com.fiap.seeking_restaurants_provider.utils;

import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Review;

public abstract class ReviewUtil {

	public static Review newReview(){
		return new Review(
				(long)1,
				"Cliente Teste",
				3,
				"Boa comida e recepção"
		);
	}

	public static Review newReview(Long id){
		return new Review(
				id,
				"Cliente Teste",
				3,
				"Boa comida e recepção"
		);
	}

	public static Review newReview(Long id, Restaurant restaurant){
		var review = new Review(
				id,
				"Cliente Teste",
				3,
				"Boa comida e recepção"
		);

		review.setRestaurant(restaurant);

		return review;
	}

}
