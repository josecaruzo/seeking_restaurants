package com.fiap.seeking_restaurants_provider.service;

import com.fiap.seeking_restaurants_provider.dto.Review.ReviewDTO;
import com.fiap.seeking_restaurants_provider.dto.Review.ReviewRestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Review;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import com.fiap.seeking_restaurants_provider.repository.ReviewRepository;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import com.fiap.seeking_restaurants_provider.utils.ReviewUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

public class ReviewServiceTest {

	private ReviewService reviewService;
	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private RestaurantRepository restaurantRepository;


	AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		reviewService = new ReviewService(reviewRepository, restaurantRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}

	@Nested
	class findReview {
		@Test
		void shouldFindByRestaurant(){
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var review1 = ReviewUtil.newReview((long)1, restaurant);
			var review2 = ReviewUtil.newReview((long)2, restaurant);

			var reviewList = Arrays.asList(review1, review2);
			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);
			Mockito.when(reviewRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(reviewList);

			//Act
			var findReviews = reviewService.findByRestaurant(restaurant.getId());

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(reviewRepository, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));
			Assertions.assertThat(findReviews)
					.hasSize(2)
					.containsExactlyInAnyOrder(ReviewRestaurantDTO.fromEntity(review1), ReviewRestaurantDTO.fromEntity(review2));
		}

		@Test
		void shouldFindAll(){
			//Arrange
			Long id = null;
			id = (long)1;
			var review1 = ReviewUtil.newReview(id);
			id = (long)2;
			var review2 = ReviewUtil.newReview(id);

			var reviewList = Arrays.asList(review1, review2);
			Mockito.when(reviewRepository.findAll()).thenReturn(reviewList);

			//Act
			var findRestaurants = reviewService.findAll();

			//Assert
			Mockito.verify(reviewRepository, Mockito.times(1)).findAll();
			Assertions.assertThat(findRestaurants)
					.hasSize(2)
					.containsExactlyInAnyOrder(ReviewRestaurantDTO.fromEntity(review1), ReviewRestaurantDTO.fromEntity(review2));
		}
	}

	@Nested
	class addReview {
		@Test
		void shouldAdd() {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var review = ReviewUtil.newReview((long)1, restaurant);
			var reviewDTO = ReviewRestaurantDTO.fromEntity(review);

			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);
			Mockito.when(reviewRepository.save(ArgumentMatchers.any(Review.class))).thenAnswer(i -> i.getArgument(0));

			//Act
			var newReview = reviewService.add(reviewDTO);

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(reviewRepository, Mockito.times(1)).save(ArgumentMatchers.any(Review.class));

			Assertions.assertThat(newReview)
					.isInstanceOf(ReviewRestaurantDTO.class)
					.isNotNull()
					.isEqualTo(ReviewRestaurantDTO.fromEntity(review));

			Assertions.assertThat(newReview.id()).isNotNull();
		}
	}

	@Nested
	class updateReview {
		@Test
		void shouldUpdate() {
			//Arrange
			var oldReview = ReviewUtil.newReview();
			var updateReview = new Review(ReviewDTO.fromEntity(oldReview));

			updateReview.setStars(updateReview.getStars() + 1);
			updateReview.setComment(updateReview.getComment() + "- Comentário atualizado ");

			Mockito.when(reviewRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(updateReview);
			Mockito.when(reviewRepository.save(ArgumentMatchers.any(Review.class))).thenAnswer(i -> i.getArgument(0));

			//Act
			var savedReview = reviewService.update(updateReview.getId(), ReviewDTO.fromEntity(updateReview));

			//Assert
			Mockito.verify(reviewRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(reviewRepository, Mockito.times(1)).save(ArgumentMatchers.any(Review.class));
			Assertions.assertThat(savedReview)
					.isInstanceOf(ReviewDTO.class)
					.isNotNull()
					.isNotEqualTo(ReviewDTO.fromEntity(oldReview));
		}
	}

	@Nested
	class deleteReview {
		@Test
		void shouldDelete() {
			// Arrange
			Long id = (long) 1;
			var restaurant = RestaurantUtil.newRestaurant();
			var review = ReviewUtil.newReview(id, restaurant);

			Mockito.when(reviewRepository.save(ArgumentMatchers.any(Review.class))).thenAnswer(i -> i.getArgument(0));
			Mockito.doNothing().when(reviewRepository).deleteById(ArgumentMatchers.any(Long.class));

			// Act
			var newReview = reviewService.add(ReviewRestaurantDTO.fromEntity(review)); //Adding a review before delete it
			reviewService.delete(newReview.id());
			// Assert
			Mockito.verify(reviewRepository, Mockito.times(1)).save(ArgumentMatchers.any(Review.class));
			Mockito.verify(reviewRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));

			Assertions.assertThat(newReview).isNotNull();
			Assertions.assertThat(newReview.id()).isEqualTo(id);
		}
	}
}
