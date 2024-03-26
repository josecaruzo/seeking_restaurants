package com.fiap.seeking_restaurants_provider.repository;

import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RestaurantRepositoryTest {
	@Mock
	private RestaurantRepository restaurantRepository;

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
	void shouldFindRestaurant(){
		//Arrange
		Long id = (long) 1;
		var restaurant = RestaurantUtil.newRestaurant(id);
		Mockito.when(restaurantRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(restaurant));

		//Act
		var optRestaurant = restaurantRepository.findById(id);

		//Assert
		Mockito.verify(restaurantRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
		Assertions.assertThat(optRestaurant)
				.isPresent()
				.containsSame(restaurant);
	}

	@Test
	void shouldFindAllRestaurants(){
		//Arrange
		Long id = null;
		id = (long) 1;
		var restaurant1 = RestaurantUtil.newRestaurant(id);
		id = (long) 2;
		var restaurant2 = RestaurantUtil.newRestaurant(id);

		var restaurantList = Arrays.asList(restaurant1, restaurant2);

		Mockito.when(restaurantRepository.findAll()).thenReturn(restaurantList);

		//Act
		var restaurants = restaurantRepository.findAll();

		//Assert
		Mockito.verify(restaurantRepository, Mockito.times(1)).findAll();
		Assertions.assertThat(restaurants)
				.hasSize(2)
				.containsExactlyInAnyOrder(restaurant1,restaurant2);
	}

	@Test
	void shouldFindByName() {
		//Arrange
		Long id = (long) 1;
		var restaurant = RestaurantUtil.newRestaurant(id);
		Mockito.when(restaurantRepository.findByName(ArgumentMatchers.any(String.class))).thenReturn(Optional.of(restaurant));

		//Act
		var optRestaurant = restaurantRepository.findByName(restaurant.getName());

		//Assert
		Mockito.verify(restaurantRepository, Mockito.times(1)).findByName(ArgumentMatchers.any(String.class));
		Assertions.assertThat(optRestaurant)
				.isPresent()
				.containsSame(restaurant);
	}

	@Test
	void shouldFindByNameContainingIgnoreCase() {
		//Arrange
		Long id = null;
		id = (long) 1;
		var restaurant1 = RestaurantUtil.newRestaurant(id);
		id = (long) 2;
		var restaurant2 = RestaurantUtil.newRestaurant(id);

		var restaurantList = Arrays.asList(restaurant1, restaurant2);
		Mockito.when(restaurantRepository.findByNameContainingIgnoreCase(ArgumentMatchers.any(String.class))).thenReturn(restaurantList);

		//Act
		var optRestaurant = restaurantRepository.findByNameContainingIgnoreCase(restaurant1.getName());

		//Assert
		Mockito.verify(restaurantRepository, Mockito.times(1)).findByNameContainingIgnoreCase(ArgumentMatchers.any(String.class));
		Assertions.assertThat(optRestaurant)
				.hasSize(2)
				.containsExactlyInAnyOrder(restaurant1,restaurant2);
	}

	@Test
	void shouldFindByAddressContainingIgnoreCase() {
		//Arrange
		Long id = null;
		id = (long) 1;
		var restaurant1 = RestaurantUtil.newRestaurant(id);
		id = (long) 2;
		var restaurant2 = RestaurantUtil.newRestaurant(id);

		var restaurantList = Arrays.asList(restaurant1, restaurant2);
		Mockito.when(restaurantRepository.findByAddressContainingIgnoreCase(ArgumentMatchers.any(String.class))).thenReturn(restaurantList);

		//Act
		var optRestaurant = restaurantRepository.findByAddressContainingIgnoreCase(restaurant1.getAddress());

		//Assert
		Mockito.verify(restaurantRepository, Mockito.times(1)).findByAddressContainingIgnoreCase(ArgumentMatchers.any(String.class));
		Assertions.assertThat(optRestaurant)
				.hasSize(2)
				.containsExactlyInAnyOrder(restaurant1,restaurant2);
	}

	@Test
	void shouldFindByTypeIgnoreCase() {
//Arrange
		Long id = null;
		id = (long) 1;
		var restaurant1 = RestaurantUtil.newRestaurant(id);
		id = (long) 2;
		var restaurant2 = RestaurantUtil.newRestaurant(id);

		var restaurantList = Arrays.asList(restaurant1, restaurant2);
		Mockito.when(restaurantRepository.findByTypeIgnoreCase(ArgumentMatchers.any(String.class))).thenReturn(restaurantList);

		//Act
		var optRestaurant = restaurantRepository.findByTypeIgnoreCase(restaurant1.getType());

		//Assert
		Mockito.verify(restaurantRepository, Mockito.times(1)).findByTypeIgnoreCase(ArgumentMatchers.any(String.class));
		Assertions.assertThat(optRestaurant)
				.hasSize(2)
				.containsExactlyInAnyOrder(restaurant1,restaurant2);
	}

	@Test
	void shouldSaveRestaurant() {
		//Arrange
		var restaurant = RestaurantUtil.newRestaurant();
		Mockito.when(restaurantRepository.save(ArgumentMatchers.any(Restaurant.class))).thenReturn(restaurant);

		//Act
		var newRestaurant = restaurantRepository.save(restaurant);

		//Assert
		Mockito.verify(restaurantRepository, Mockito.times(1)).save(ArgumentMatchers.any(Restaurant.class));
		//NewRestaurant is not null
		Assertions.assertThat(newRestaurant)
				.isInstanceOf(Restaurant.class)
				.isNotNull()
				.isEqualTo(restaurant);
		//New restaurant id is not null, so successfully created auto increment id
		Assertions.assertThat(newRestaurant)
				.extracting(Restaurant::getId)
				.isNotNull();
	}

	@Test
	void shouldDeleteRestaurant(){
		// Arrange
		Long id = (long) 1;
		Mockito.doNothing().when(restaurantRepository).deleteById(ArgumentMatchers.any(Long.class));
		// Act
		restaurantRepository.deleteById(id);
		var findRestaurant = restaurantRepository.findById(id);
		// Assert
		Mockito.verify(restaurantRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));
		Mockito.verify(restaurantRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
		Assertions.assertThat(findRestaurant).isNotPresent();
	}
}
