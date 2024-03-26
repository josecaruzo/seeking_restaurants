package com.fiap.seeking_restaurants_provider.repository;

import com.fiap.seeking_restaurants_provider.entity.Calendar;
import com.fiap.seeking_restaurants_provider.utils.CalendarUtil;
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

public class CalendarRepositoryTest {
	@Mock
	private CalendarRepository calendarRepository;

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
	void shouldFindCalendar(){
		//Arrange
		Long id = (long) 1;
		var calendar = CalendarUtil.newCalendar(id);
		Mockito.when(calendarRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(calendar));

		//Act
		var optCalendar = calendarRepository.findById(id);

		//Assert
		Mockito.verify(calendarRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
		Assertions.assertThat(optCalendar)
				.isPresent()
				.containsSame(calendar);
	}

	@Test
	void shouldFindAllCalendars(){
		//Arrange
		Long id = null;
		id = (long) 1;
		var calendar1 = CalendarUtil.newCalendar(id);
		id = (long) 2;
		var calendar2 = CalendarUtil.newCalendar(id);

		var calendarList = Arrays.asList(calendar1, calendar2);

		Mockito.when(calendarRepository.findAll()).thenReturn(calendarList);

		//Act
		var calendars = calendarRepository.findAll();

		//Assert
		Mockito.verify(calendarRepository, Mockito.times(1)).findAll();
		Assertions.assertThat(calendars)
				.hasSize(2)
				.containsExactlyInAnyOrder(calendar1,calendar2);
	}

	@Test
	void shouldSaveCalendar() {
		//Arrange
		var calendar = CalendarUtil.newCalendar();
		Mockito.when(calendarRepository.save(ArgumentMatchers.any(Calendar.class))).thenReturn(calendar);

		//Act
		var newCalendar = calendarRepository.save(calendar);

		//Assert
		Mockito.verify(calendarRepository, Mockito.times(1)).save(ArgumentMatchers.any(Calendar.class));
		//NewCalendar is not null
		Assertions.assertThat(newCalendar)
				.isInstanceOf(Calendar.class)
				.isNotNull()
				.isEqualTo(calendar);
		//New calendar id is not null, so successfully created auto increment id
		Assertions.assertThat(newCalendar)
				.extracting(Calendar::getId)
				.isNotNull();
	}

	@Test
	void shouldDeleteCalendar(){
		// Arrange
		Long id = (long) 1;
		Mockito.doNothing().when(calendarRepository).deleteById(ArgumentMatchers.any(Long.class));
		// Act
		calendarRepository.deleteById(id);
		var findCalendar = calendarRepository.findById(id);
		// Assert
		Mockito.verify(calendarRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));
		Mockito.verify(calendarRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
		Assertions.assertThat(findCalendar).isNotPresent();
	}
}
