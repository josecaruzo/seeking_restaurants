package com.fiap.seeking_restaurants_provider.service;

import com.fiap.seeking_restaurants_provider.controller.exception.DatabaseException;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantCalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.repository.CalendarRepository;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
	private final RestaurantRepository restaurantRepository;


	@Autowired
	public RestaurantService(RestaurantRepository restaurantRepository, CalendarRepository calendarRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	public Collection<RestaurantCalendarDTO> findAll(){
		var restaurants = restaurantRepository.findAll();
		return restaurants.stream().map(RestaurantCalendarDTO::fromEntity).collect(Collectors.toList());
	}

	public Collection<RestaurantCalendarDTO> findByName(String name){
		var restaurants = restaurantRepository.findByNameContainingIgnoreCase(name);
		return restaurants.stream().map(RestaurantCalendarDTO::fromEntity).collect(Collectors.toList());
	}
	public Collection<RestaurantCalendarDTO> findByAddress(String address){
		var restaurants = restaurantRepository.findByAddressContainingIgnoreCase(address);
		return restaurants.stream().map(RestaurantCalendarDTO::fromEntity).collect(Collectors.toList());
	}

	public Collection<RestaurantCalendarDTO> findByType(String type){
		var restaurants = restaurantRepository.findByTypeIgnoreCase(type);
		return restaurants.stream().map(RestaurantCalendarDTO::fromEntity).collect(Collectors.toList());
	}

	public RestaurantDTO add(RestaurantDTO restaurantDTO) {
		Restaurant restaurant = RestaurantDTO.toEntity(restaurantDTO);
		var newRestaurant = restaurantRepository.save(restaurant);

		return RestaurantDTO.fromEntity(newRestaurant);
	}

	public RestaurantDTO update(Long id, RestaurantDTO restaurantDTO){
		try{
			Restaurant updateRestaurant = restaurantRepository.getReferenceById(id);

			updateRestaurant.setName(restaurantDTO.name());
			updateRestaurant.setZipcode(restaurantDTO.zipcode());
			updateRestaurant.setAddress(restaurantDTO.address());
			updateRestaurant.setType(restaurantDTO.type());
			updateRestaurant.setCapacity(restaurantDTO.capacity());

			updateRestaurant = restaurantRepository.save(updateRestaurant);
			return RestaurantDTO.fromEntity(updateRestaurant);
		}catch ( DataIntegrityViolationException | LazyInitializationException e ) {
			throw new EntityNotFoundException("Restaurante não encontrado"); // Restaurant not found
		}
	}

	public void delete(Long id){
		try{
			restaurantRepository.deleteById(id);
		}catch ( DataIntegrityViolationException | LazyInitializationException e) {
			throw new EntityNotFoundException("Restaurante não encontrado"); // Restaurant not found
		}
	}
}
