package com.fiap.seeking_restaurants_provider.repository;

import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Table;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import com.fiap.seeking_restaurants_provider.utils.TableUtil;
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

public class TableRepositoryTest {

	@Mock
	private TableRepository tableRepository;

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
	void shouldFindTable(){
		//Arrange
		Long id = (long) 1;
		var table = TableUtil.newTable(id);
		Mockito.when(tableRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(table));

		//Act
		var optTable = tableRepository.findById(id);

		//Assert
		Mockito.verify(tableRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
		Assertions.assertThat(optTable)
				.isPresent()
				.containsSame(table);
	}

	@Test
	void shouldFindAllTables(){
		//Arrange
		Long id = null;
		id = (long) 1;
		var table1 = TableUtil.newTable(id);
		id = (long) 2;
		var table2 = TableUtil.newTable(id);

		var tableList = Arrays.asList(table1, table2);

		Mockito.when(tableRepository.findAll()).thenReturn(tableList);

		//Act
		var tables = tableRepository.findAll();

		//Assert
		Mockito.verify(tableRepository, Mockito.times(1)).findAll();
		Assertions.assertThat(tables)
				.hasSize(2)
				.containsExactlyInAnyOrder(table1,table2);
	}

	@Test
	void shouldFindByRestaurant() {
		//Arrange
		Long id = null;
		var restaurant = RestaurantUtil.newRestaurant();
		id = (long) 1;
		var table1 = TableUtil.newTable(id, 1, restaurant);
		id = (long) 2;
		var table2 = TableUtil.newTable(id, 2, restaurant);

		var tableList = Arrays.asList(table1, table2);

		Mockito.when(tableRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(tableList);

		//Act
		var tables = tableRepository.findByRestaurant(restaurant);

		//Assert
		Mockito.verify(tableRepository, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));
		Assertions.assertThat(tables)
				.hasSize(2)
				.containsExactlyInAnyOrder(table1,table2);
	}

	@Test
	void shouldSaveTable() {
		//Arrange
		var table = TableUtil.newTable();
		Mockito.when(tableRepository.save(ArgumentMatchers.any(Table.class))).thenReturn(table);

		//Act
		var newTable = tableRepository.save(table);

		//Assert
		Mockito.verify(tableRepository, Mockito.times(1)).save(ArgumentMatchers.any(Table.class));
		//NewTable is not null
		Assertions.assertThat(newTable)
				.isInstanceOf(Table.class)
				.isNotNull()
				.isEqualTo(table);
		//New table id is not null, so successfully created auto increment id
		Assertions.assertThat(newTable)
				.extracting(Table::getId)
				.isNotNull();
	}

	@Test
	void shouldDeleteTable(){
		// Arrange
		Long id = (long) 1;
		Mockito.doNothing().when(tableRepository).deleteById(ArgumentMatchers.any(Long.class));
		// Act
		tableRepository.deleteById(id);
		var findTable = tableRepository.findById(id);
		// Assert
		Mockito.verify(tableRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));
		Mockito.verify(tableRepository, Mockito.times(1)).findById(ArgumentMatchers.any(Long.class));
		Assertions.assertThat(findTable).isNotPresent();
	}
}
