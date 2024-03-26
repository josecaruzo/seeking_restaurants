package com.fiap.seeking_restaurants_provider.service;

import com.fiap.seeking_restaurants_provider.controller.exception.DatabaseException;
import com.fiap.seeking_restaurants_provider.dto.Table.TableDTO;
import com.fiap.seeking_restaurants_provider.dto.Table.TableRestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Table;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import com.fiap.seeking_restaurants_provider.repository.TableRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class TableService {
	private final TableRepository tableRepository;
	private final RestaurantRepository restaurantRepository;

	@Autowired
	public TableService(TableRepository tableRepository, RestaurantRepository restaurantRepository){
		this.tableRepository = tableRepository;
		this.restaurantRepository = restaurantRepository;
	}

	public Collection<TableRestaurantDTO> findByRestaurant(Long restaurant_id){
		try{
			Restaurant restaurant = restaurantRepository.getReferenceById(restaurant_id);
			var tables = tableRepository.findByRestaurant(restaurant);
			return tables.stream().map(TableRestaurantDTO::fromEntity).collect(Collectors.toList());
		}catch (DataIntegrityViolationException | LazyInitializationException e) {
			throw new EntityNotFoundException("Restaurante não encontrado"); // Restaurant not found
		}
	}
	public TableRestaurantDTO add(TableRestaurantDTO tableDTO) {
		try{
			//Get Restaurant and all tables saved
			Restaurant restaurant = restaurantRepository.getReferenceById(tableDTO.restaurant_id());
			var tables = tableRepository.findByRestaurant(restaurant); //To confirm correct restaurant capacity

			//Add one table to restaurant capacity and update restaurant
			restaurant.setCapacity(tables.size() + 1);
			var UpdatedRestaurant = restaurantRepository.save(restaurant);

			//Save new table
			Table table = TableRestaurantDTO.toEntity(tableDTO, UpdatedRestaurant);
			var newTable = tableRepository.save(table);

			return TableRestaurantDTO.fromEntity(newTable);
		}catch (DataIntegrityViolationException | LazyInitializationException e) {
			throw new EntityNotFoundException("Restaurante não encontrado"); // Restaurant not found
		}
	}

	public Collection<TableRestaurantDTO> addAll(Long restaurant_id) {
		try{
			Restaurant restaurant = restaurantRepository.getReferenceById(restaurant_id);
			var tables = tableRepository.findByRestaurant(restaurant); //To confirm correct restaurant capacity

			if(tables.isEmpty()) {
				Collection<Table> newTables = new ArrayList<>();

				for (int i = 1; i <= restaurant.getCapacity(); i++) {
					Table table = new Table();
					table.setNumber(i);
					table.setCapacity(4); // default capacity as 4
					table.setRestaurant(restaurant);

					newTables.add(table);
				}

				var savedTables = tableRepository.saveAll(newTables);

				return savedTables.stream().map(TableRestaurantDTO::fromEntity).collect(Collectors.toList());


			}
			else{
				throw new DatabaseException("Mesas já cadastradas"); // Tables added already
			}
		}catch (DataIntegrityViolationException | LazyInitializationException e) {
			throw new EntityNotFoundException("Restaurante não encontrado"); // Restaurant not found
		}
	}

	public TableDTO update(Long id, TableDTO tableDTO) {
		try{
			Table updateTable = tableRepository.getReferenceById(id);

			updateTable.setNumber(tableDTO.number());
			updateTable.setCapacity(tableDTO.capacity());

			updateTable = tableRepository.save(updateTable);

			return TableDTO.fromEntity(updateTable);
		}catch (DataIntegrityViolationException | LazyInitializationException e) {
			throw new EntityNotFoundException("Mesa não encontrada"); // Table not found
		}
	}

	public void delete(Long id){
		try{
			Table table = tableRepository.getReferenceById(id);

			//Get Restaurant
			Restaurant restaurant = table.getRestaurant();
			var tables = tableRepository.findByRestaurant(restaurant); //To confirm correct restaurant capacity

			//Remove one table from restaurant capacity and update restaurant
			restaurant.setCapacity(tables.size() - 1);
			var UpdatedRestaurant = restaurantRepository.save(restaurant);

			//Delete table
			tableRepository.deleteById(id);

		}catch (DataIntegrityViolationException | LazyInitializationException e) {
			throw new EntityNotFoundException("Mesa não encontrada"); // Table not found
		}
	}
}
