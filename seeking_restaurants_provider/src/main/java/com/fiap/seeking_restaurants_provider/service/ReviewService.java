package com.fiap.seeking_restaurants_provider.service;

import com.fiap.seeking_restaurants_provider.controller.exception.DatabaseException;
import com.fiap.seeking_restaurants_provider.dto.Review.ReviewDTO;
import com.fiap.seeking_restaurants_provider.dto.Review.ReviewRestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Review;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import com.fiap.seeking_restaurants_provider.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final RestaurantRepository restaurantRepository;

	@Autowired
	public ReviewService(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository){
		this.reviewRepository = reviewRepository;
		this.restaurantRepository = restaurantRepository;
	}

	public Collection<ReviewRestaurantDTO> findAll(){
		var reviews = reviewRepository.findAll();
		return reviews.stream().map(ReviewRestaurantDTO::fromEntity).collect(Collectors.toList());
	}

	public Collection<ReviewRestaurantDTO> findByRestaurant(Long restaurant_id){
		try{
			Restaurant restaurant = restaurantRepository.getReferenceById(restaurant_id);
			var reviews = reviewRepository.findByRestaurant(restaurant);
			return reviews.stream().map(ReviewRestaurantDTO::fromEntity).collect(Collectors.toList());
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Restaurante não encontrado"); // Restaurant not found
		}
	}
	public ReviewRestaurantDTO add(ReviewRestaurantDTO dto) {
		try{
			Restaurant restaurant = restaurantRepository.getReferenceById(dto.restaurant_id());
			Review review = ReviewRestaurantDTO.toEntity(dto, restaurant);
			var newReview = reviewRepository.save(review);

			return ReviewRestaurantDTO.fromEntity(newReview);
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Restaurante não encontrado"); // Restaurant not found
		}
	}

	public ReviewDTO update(Long id, ReviewDTO reviewDTO) {
		try{
			Review updateReview = reviewRepository.getReferenceById(id);

			updateReview.setGuestName(reviewDTO.guestName());
			updateReview.setStars(reviewDTO.stars());
			updateReview.setComment(reviewDTO.comment());

			updateReview = reviewRepository.save(updateReview);

			return ReviewDTO.fromEntity(updateReview);
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Mesa não encontrada"); // Table not found
		}
	}

	public void delete(Long id){
		reviewRepository.deleteById(id);
	}
}
