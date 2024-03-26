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

	private final ReviewService reviewService;

	@Autowired
	public ReviewController(ReviewService reviewService){
		this.reviewService = reviewService;
	}

	@GetMapping
	public ResponseEntity<Collection<ReviewRestaurantDTO>> findAll(){
		var reviews = reviewService.findAll();
		return ResponseEntity.ok(reviews);
	}
	@GetMapping("/{restaurantId}")
	public ResponseEntity<Collection<ReviewRestaurantDTO>> findByRestaurant(@PathVariable Long restaurantId){
		var reviews = reviewService.findByRestaurant(restaurantId);
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
	public ResponseEntity<String> delete(@PathVariable Long id){
		reviewService.delete(id);
		return ResponseEntity.ok("Review " + id + " deleted");
	}
}
