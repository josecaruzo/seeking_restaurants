package com.fiap.seeking_restaurants_provider.controller;

import com.fiap.seeking_restaurants_provider.dto.Review.ReviewDTO;
import com.fiap.seeking_restaurants_provider.dto.Review.ReviewRestaurantDTO;
import com.fiap.seeking_restaurants_provider.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
	@Autowired
	private ReviewService reviewService;
	@GetMapping
	public ResponseEntity<Collection<ReviewRestaurantDTO>> findAll(){
		var reviews = reviewService.findAll();
		return ResponseEntity.ok(reviews);
	}
	@GetMapping("/{restaurant_id}")
	public ResponseEntity<Collection<ReviewRestaurantDTO>> findByRestaurant(@PathVariable Long restaurant_id){
		var reviews = reviewService.findByRestaurant(restaurant_id);
		return ResponseEntity.ok(reviews);
	}
	@PostMapping
	public ResponseEntity<ReviewRestaurantDTO> add(@Valid @RequestBody ReviewRestaurantDTO reviewDTO){
		var newReview = reviewService.add(reviewDTO);
		return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(newReview);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ReviewDTO> update(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO){
		ReviewDTO updateReview = reviewService.update(id, reviewDTO);
		return ResponseEntity.ok(updateReview);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		reviewService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
