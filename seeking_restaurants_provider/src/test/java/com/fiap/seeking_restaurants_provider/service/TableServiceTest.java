package com.fiap.seeking_restaurants_provider.service;

import com.fiap.seeking_restaurants_provider.dto.Table.TableDTO;
import com.fiap.seeking_restaurants_provider.dto.Table.TableRestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Table;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import com.fiap.seeking_restaurants_provider.repository.TableRepository;
import com.fiap.seeking_restaurants_provider.utils.RestaurantUtil;
import com.fiap.seeking_restaurants_provider.utils.TableUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

public class TableServiceTest {

	private TableService tableService;
	@Mock
	private TableRepository tableRepository;

	@Mock
	private RestaurantRepository restaurantRepository;


	AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		tableService = new TableService(tableRepository, restaurantRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
		mocks.close();
	}

	@Nested
	class findTable {
		@Test
		void shouldFindByRestaurant(){
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var table1 = TableUtil.newTable((long)1, 1, restaurant);
			var table2 = TableUtil.newTable((long)2, 2, restaurant);

			var tableList = Arrays.asList(table1, table2);
			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);
			Mockito.when(tableRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(tableList);

			//Act
			var findTables = tableService.findByRestaurant(restaurant.getId());

			//Assert
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(tableRepository, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));
			Assertions.assertThat(findTables)
					.hasSize(2)
					.containsExactlyInAnyOrder(TableRestaurantDTO.fromEntity(table1), TableRestaurantDTO.fromEntity(table2));
		}
	}

	@Nested
	class addTable {
		@Test
		void shouldAdd() {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var updatedRestaurant = new Restaurant();
			updatedRestaurant = restaurant;
			//We are only adding 1 table
			updatedRestaurant.setCapacity(1);

			var table = TableUtil.newTable((long) 1, 1, updatedRestaurant);

			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);
			Mockito.when(restaurantRepository.save(ArgumentMatchers.any(Restaurant.class))).thenReturn(updatedRestaurant);
			Mockito.when(tableRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(Collections.emptyList());
			Mockito.when(tableRepository.save(ArgumentMatchers.any(Table.class))).thenReturn(table);

			//Act
			var newTable = tableService.add(TableRestaurantDTO.fromEntity(table));

			//Arrange
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(restaurantRepository, Mockito.times(1)).save(ArgumentMatchers.any(Restaurant.class));
			Mockito.verify(tableRepository, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));
			Mockito.verify(tableRepository, Mockito.times(1)).save(ArgumentMatchers.any(Table.class));

			Assertions.assertThat(newTable)
					.isInstanceOf(TableRestaurantDTO.class)
					.isNotNull()
					.isEqualTo(TableRestaurantDTO.fromEntity(table));

			Assertions.assertThat(newTable.id()).isNotNull();
		}

		@Test
		void shouldAddAll() {
			//Arrange
			var restaurant = RestaurantUtil.newRestaurant();
			var tables = TableUtil.newTables(restaurant);

			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);
			Mockito.when(tableRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(Collections.emptyList());
			Mockito.when(tableRepository.saveAll(ArgumentMatchers.anyCollection())).thenReturn(tables);


			//Act
			var newTables = tableService.addAll(restaurant.getId());


			//Arrange
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(tableRepository, Mockito.times(1)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));
			Mockito.verify(tableRepository, Mockito.times(1)).saveAll(ArgumentMatchers.anyCollection());
			Assertions.assertThat(newTables)
					.hasSize(restaurant.getCapacity())
					.isNotNull();
		}
	}

	@Nested
	class updateTable {
		@Test
		void shouldUpdate() {
			//Arrange
			var oldTable = TableUtil.newTable();
			var updateTable = new Table(TableDTO.fromEntity(oldTable));
			updateTable.setCapacity(updateTable.getCapacity()+2);

			Mockito.when(tableRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(updateTable);
			Mockito.when(tableRepository.save(ArgumentMatchers.any(Table.class))).thenAnswer(i -> i.getArgument(0));

			//Act
			var savedTable = tableService.update(updateTable.getId(), TableDTO.fromEntity(updateTable));

			//Assert
			Mockito.verify(tableRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(tableRepository, Mockito.times(1)).save(ArgumentMatchers.any(Table.class));
			Assertions.assertThat(savedTable)
					.isInstanceOf(TableDTO.class)
					.isNotNull()
					.isNotEqualTo(TableDTO.fromEntity(oldTable));
		}
	}

	@Nested
	class deleteTable {
		@Test
		void shouldDelete() {
			//Arrange
			Long id = (long) 1;
			var restaurant = RestaurantUtil.newRestaurant();
			restaurant.setCapacity(restaurant.getCapacity() - 1 );
			
			var table = TableUtil.newTable(id, 1, restaurant);
			var tableDTO = TableRestaurantDTO.fromEntity(table);

			Mockito.when(restaurantRepository.save(ArgumentMatchers.any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));
			Mockito.when(restaurantRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(restaurant);

			Mockito.when(tableRepository.save(ArgumentMatchers.any(Table.class))).thenAnswer(i -> i.getArgument(0));
			Mockito.when(tableRepository.getReferenceById(ArgumentMatchers.any(Long.class))).thenReturn(table);
			Mockito.when(tableRepository.findByRestaurant(ArgumentMatchers.any(Restaurant.class))).thenReturn(Collections.emptyList());
			Mockito.doNothing().when(tableRepository).deleteById(ArgumentMatchers.any(Long.class));

			// Act
			var newTable = tableService.add(tableDTO); //Adding a table before delete it
			tableService.delete(newTable.id());

			// Assert
			Mockito.verify(restaurantRepository, Mockito.times(2)).save(ArgumentMatchers.any(Restaurant.class));
			Mockito.verify(restaurantRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));

			Mockito.verify(tableRepository, Mockito.times(1)).save(ArgumentMatchers.any(Table.class));
			Mockito.verify(tableRepository, Mockito.times(1)).getReferenceById(ArgumentMatchers.any(Long.class));
			Mockito.verify(tableRepository, Mockito.times(2)).findByRestaurant(ArgumentMatchers.any(Restaurant.class));
			Mockito.verify(tableRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any(Long.class));

			Assertions.assertThat(newTable).isNotNull();
			Assertions.assertThat(newTable.id()).isEqualTo(id);
		}
	}
}
