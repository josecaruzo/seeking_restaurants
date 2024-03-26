package com.fiap.seeking_restaurants_provider.repository;

import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Review;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import com.fiap.seeking_restaurants_provider.utils.ReviewUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Optional;

public class ReviewRepositoryTest {
	@Mock
	private ReviewRepository reviewRepository;

	AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}

	@Test
	void shouldFindReview(){
		//Arrange
		Long id = (long) 1;
		var review = ReviewUtil.newReview(id);
		Mockito.when(reviewRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(review));

		//Act
		var optReview = reviewRepository.findById(id);

		//Assert
		Mockito.verify(reviewRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
		Assertions.assertThat(optReview)
				.isPresent()
				.containsSame(review);
	}

	@Test
	void shouldFindAllReviews(){
		//Arrange
		Long id = null;
		id = (long) 1;
		var review1 = ReviewUtil.newReview(id);
		id = (long) 2;
		var review2 = ReviewUtil.newReview(id);

		var reviewList = Arrays.asList(review1, review2);

		Mockito.when(reviewRepository.findAll()).thenReturn(reviewList);

		//Act
		var reviews = reviewRepository.findAll();

		//Assert
		Mockito.verify(reviewRepository, Mockito.times(1)).findAll();
		Assertions.assertThat(reviews)
				.hasSize(2)
				.containsExactlyInAnyOrder(review1,review2);
	}

	@Test
	void shouldFindByRestaurant(){
		//Arrange
		Long id = null;
		var restaurant = RestaurantUtil.newRestaurant();
		id = (long) 1;
		var review1 = ReviewUtil.newReview(id, restaurant);
		id = (long) 2;
		var review2 = ReviewUtil.newReview(id, restaurant);

		var reviewList = Arrays.asList(review1, review2);

		Mockito.when(reviewRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(reviewList);

		//Act
		var reviews = reviewRepository.findByRestaurant(restaurant);

		//Assert
		Mockito.verify(reviewRepository, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));
		Assertions.assertThat(reviews)
				.hasSize(2)
				.containsExactlyInAnyOrder(review1,review2);
	}

	@Test
	void shouldSaveReview() {
		//Arrange
		var review = ReviewUtil.newReview();
		Mockito.when(reviewRepository.save(ArgumentMatchers.any(Review.class))).thenReturn(review);

		//Act
		var newReview = reviewRepository.save(review);

		//Assert
		Mockito.verify(reviewRepository, Mockito.times(1)).save(ArgumentMatchers.any(Review.class));
			//NewReview is not null
			Assertions.assertThat(newReview)
					.isInstanceOf(Review.class)
					.isNotNull()
					.isEqualTo(review);
			//New review id is not null, so successfully created auto increment id
			Assertions.assertThat(newReview)
					.extracting(Review::getId)
					.isNotNull();
	}

	@Test
	void shouldDeleteReview(){
		// Arrange
		Long id = (long) 1;
		Mockito.doNothing().when(reviewRepository).deleteById(ArgumentMatchers.any(Long.class));
		// Act
		reviewRepository.deleteById(id);
		var findReview = reviewRepository.findById(id);
		// Assert
		Mockito.verify(reviewRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));
		Mockito.verify(reviewRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
		Assertions.assertThat(findReview).isNotPresent();
	}
}
