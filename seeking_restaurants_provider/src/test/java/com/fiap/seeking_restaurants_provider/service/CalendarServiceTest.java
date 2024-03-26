package com.fiap.seeking_restaurants_provider.service;

import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarRestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Calendar;
import com.fiap.seeking_restaurants_provider.repository.CalendarRepository;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import com.fiap.seeking_restaurants_provider.utils.CalendarUtil;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CalendarServiceTest {
	private CalendarService calendarService;

	@Mock
	private CalendarRepository calendarRepository;
	@Mock
	private RestaurantRepository restaurantRepository;

	AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		calendarService = new CalendarService(calendarRepository, restaurantRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}
	@Nested
	class addCalendar {
		@Test
		void shouldAdd() {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var calendar = CalendarUtil.newCalendar(restaurant);

			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);
			Mockito.when(calendarRepository.save(ArgumentMatchers.any(Calendar.class))).thenAnswer(i -> i.getArgument(0));

			//Act
			var newCalendar = calendarService.add(CalendarRestaurantDTO.fromEntity(calendar));

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(calendarRepository, Mockito.times(1)).save(ArgumentMatchers.any(Calendar.class));

			Assertions.assertThat(newCalendar)
					.isInstanceOf(CalendarRestaurantDTO.class)
					.isNotNull()
					.isEqualTo(CalendarRestaurantDTO.fromEntity(calendar));

			Assertions.assertThat(newCalendar.id()).isNotNull();
		}
	}

	@Nested
	class updateCalendar {
		@Test
		void shouldUpdate() {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var oldCalendar = CalendarUtil.newCalendar(restaurant);

			var updateCalendar = new Calendar(CalendarDTO.fromEntity(oldCalendar));
			updateCalendar.setSaturday_hours("closed");
			updateCalendar.setSunday_hours("closed");

			Mockito.when(calendarRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(updateCalendar);
			Mockito.when(calendarRepository.save(ArgumentMatchers.any(Calendar.class))).thenAnswer(i -> i.getArgument(0));

			//Act
			var savedCalendar = calendarService.update(updateCalendar.getId(), CalendarDTO.fromEntity(updateCalendar));

			//Assert
			Mockito.verify(calendarRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(calendarRepository, Mockito.times(1)).save(ArgumentMatchers.any(Calendar.class));
			Assertions.assertThat(savedCalendar)
					.isInstanceOf(CalendarDTO.class)
					.isNotNull()
					.isNotEqualTo(CalendarDTO.fromEntity(oldCalendar));
		}
	}
}
