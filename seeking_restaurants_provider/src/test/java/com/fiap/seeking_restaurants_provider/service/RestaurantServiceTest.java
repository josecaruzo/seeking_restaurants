package com.fiap.seeking_restaurants_provider.service;

import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantCalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.repository.CalendarRepository;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

public class RestaurantServiceTest {

	private RestaurantService restaurantService;
	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private CalendarRepository calendarRepository;
	AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		restaurantService = new RestaurantService(restaurantRepository, calendarRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}

	@Nested
	class findRestaurant{
		@Test
		void shouldFindAll(){
			//Arrange
			Long id = null;
			id = (long)1;
			var restaurant1 = RestaurantUtil.newRestaurant(id);
			id = (long)2;
			var restaurant2 = RestaurantUtil.newRestaurant(id);

			var restaurantList = Arrays.asList(restaurant1, restaurant2);
			Mockito.when(restaurantRepository.findAll()).thenReturn(restaurantList);

			//Act
			var findRestaurants = restaurantService.findAll();

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).findAll();
			Assertions.assertThat(findRestaurants)
					.hasSize(2)
					.containsExactlyInAnyOrder(RestaurantCalendarDTO.fromEntity(restaurant1), RestaurantCalendarDTO.fromEntity(restaurant2));
		}
		@Test
		void shouldFindByName(){
			//Arrange
			String name = null;

			name = "Restaurant Test - USA";
			var restaurant1 = RestaurantUtil.newRestaurantByName((long)1, name);
			name = "Restaurant Test - Brazil";
			var restaurant2 = RestaurantUtil.newRestaurantByName((long)2, name);

			var restaurantList = Arrays.asList(restaurant1, restaurant2);
			Mockito.when(restaurantRepository.findByNameContainingIgnoreCase(ArgumentMatchers.any(String.class))).thenReturn(restaurantList);

			//Act
			name = "Restaurant";
			var findRestaurants = restaurantService.findByName(name);

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).findByNameContainingIgnoreCase(ArgumentMatchers.any(String.class));
			Assertions.assertThat(findRestaurants)
					.hasSize(2)
					.containsExactlyInAnyOrder(RestaurantCalendarDTO.fromEntity(restaurant1),RestaurantCalendarDTO.fromEntity(restaurant2));
		}

		@Test
		void shouldFindByType(){
			//Arrange
			String type = null;
			type = "Pizzaria";
			var restaurant1 = RestaurantUtil.newRestaurantByType((long)1, type);
			type = "Pizzaria";
			var restaurant2 = RestaurantUtil.newRestaurantByType((long)2, type);

			var restaurantList = Arrays.asList(restaurant1, restaurant2);
			Mockito.when(restaurantRepository.findByTypeIgnoreCase(ArgumentMatchers.any(String.class))).thenReturn(restaurantList);

			//Act
			type = "Pizzaria"; //Type is match case
			var findRestaurants = restaurantService.findByType(type);

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).findByTypeIgnoreCase(ArgumentMatchers.any(String.class));
			Assertions.assertThat(findRestaurants)
					.hasSize(2)
					.containsExactlyInAnyOrder(RestaurantCalendarDTO.fromEntity(restaurant1),RestaurantCalendarDTO.fromEntity(restaurant2));
		}

		@Test
		void shouldFindByAddress(){
			//Arrange
			String address = null;
			address = "Street 1, Campinas - SP, Brazil";
			var restaurant1 = RestaurantUtil.newRestaurantByType((long)1, address);
			address = "Street 2, Campinas - SP, Brazil";
			var restaurant2 = RestaurantUtil.newRestaurantByType((long)2, address);

			var restaurantList = Arrays.asList(restaurant1, restaurant2);
			Mockito.when(restaurantRepository.findByAddressContainingIgnoreCase(ArgumentMatchers.any(String.class))).thenReturn(restaurantList);

			//Act
			address = "Campinas - SP";
			var findRestaurants = restaurantService.findByAddress(address);

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).findByAddressContainingIgnoreCase(ArgumentMatchers.any(String.class));
			Assertions.assertThat(findRestaurants)
					.hasSize(2)
					.containsExactlyInAnyOrder(RestaurantCalendarDTO.fromEntity(restaurant1), RestaurantCalendarDTO.fromEntity(restaurant2));
		}
	}

	@Nested
	class addRestaurant {
		@Test
		void shouldAdd() {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var restaurantDTO = RestaurantDTO.fromEntity(restaurant);
			Mockito.when(restaurantRepository.save(ArgumentMatchers.any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));

			//Act
			var newRestaurant = restaurantService.add(restaurantDTO);

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).save(ArgumentMatchers.any(Restaurant.class));

			Assertions.assertThat(newRestaurant)
					.isInstanceOf(RestaurantDTO.class)
					.isNotNull()
					.isEqualTo(RestaurantDTO.fromEntity(restaurant));

			Assertions.assertThat(newRestaurant.id()).isNotNull();
		}
	}

	@Nested
	class updateRestaurant {
		@Test
		void shouldUpdate() {
			//Arrange
			var oldRestaurant = RestaurantUtil.newRestaurant();
			var updateRestaurant = new Restaurant(RestaurantDTO.fromEntity(oldRestaurant));
			updateRestaurant.setName(updateRestaurant.getName() + " New name");
			updateRestaurant.setCapacity(updateRestaurant.getCapacity() + 2);

			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(updateRestaurant);
			Mockito.when(restaurantRepository.save(ArgumentMatchers.any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));

			//Act
			var savedRestaurant = restaurantService.update(updateRestaurant.getId(), RestaurantDTO.fromEntity(updateRestaurant));

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(restaurantRepository, Mockito.times(1)).save(ArgumentMatchers.any(Restaurant.class));
			Assertions.assertThat(savedRestaurant)
					.isInstanceOf(RestaurantDTO.class)
					.isNotNull()
					.isNotEqualTo(RestaurantDTO.fromEntity(oldRestaurant));
		}
	}

	@Nested
	class deleteRestaurant {
		@Test
		void shouldDelete() {
			// Arrange
			Long id = (long) 1;
			var restaurant = RestaurantUtil.newRestaurant(id);

			Mockito.when(restaurantRepository.save(ArgumentMatchers.any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));
			Mockito.doNothing().when(restaurantRepository).deleteById(ArgumentMatchers.any(Long.class));

			// Act
			var newRestaurant = restaurantService.add(RestaurantDTO.fromEntity(restaurant)); //Adding a restaurant before delete it
			restaurantService.delete(newRestaurant.id());
			// Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).save(ArgumentMatchers.any(Restaurant.class));
			Mockito.verify(restaurantRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));

			Assertions.assertThat(newRestaurant).isNotNull();
			Assertions.assertThat(newRestaurant.id()).isEqualTo(id);
		}
	}
}
