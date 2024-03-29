package com.fiap.seeking_restaurants_provider.controller;

import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarRestaurantDTO;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantCalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantDTO;
import com.fiap.seeking_restaurants_provider.entity.Calendar;
import com.fiap.seeking_restaurants_provider.service.CalendarService;
import com.fiap.seeking_restaurants_provider.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
	private final RestaurantService restaurantService;

	private final CalendarService calendarService;

	@Autowired
	public RestaurantController(RestaurantService restaurantService, CalendarService calendarService){
		this.restaurantService = restaurantService;
		this.calendarService = calendarService;
	}

	@GetMapping
	public ResponseEntity<Collection<RestaurantCalendarDTO>> findAll(){
		var restaurants = restaurantService.findAll();
		return ResponseEntity.ok(restaurants);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<Collection<RestaurantCalendarDTO>> findByName(@PathVariable String name){
		var restaurants = restaurantService.findByName(name);
		return ResponseEntity.ok(restaurants);
	}

	@GetMapping("/location/{address}")
	public ResponseEntity<Collection<RestaurantCalendarDTO>> findByAddress(@PathVariable String address){
		var restaurants = restaurantService.findByAddress(address);
		return ResponseEntity.ok(restaurants);
	}

	@GetMapping("/type/{type}")
	public ResponseEntity<Collection<RestaurantCalendarDTO>> findByType(@PathVariable String type){
		var restaurants = restaurantService.findByType(type);
		return ResponseEntity.ok(restaurants);
	}

	@PostMapping
	public ResponseEntity<RestaurantCalendarDTO> add(@Valid @RequestBody RestaurantCalendarDTO restaurantDTO){
		//Adding new restaurant
		var restaurant = RestaurantDTO.fromDTO(restaurantDTO);
		var newRestaurant = RestaurantDTO.toEntity(restaurantService.add(restaurant));

		//Adding new restaurant calendar
		Calendar calendar = new Calendar(restaurantDTO.calendar());
		calendar.setRestaurant(newRestaurant);
		var newCalendar = CalendarRestaurantDTO.toEntity(calendarService.add(CalendarRestaurantDTO.fromEntity(calendar)), newRestaurant);

		//Constructing response return with saved data
		newRestaurant.setCalendar(newCalendar);

		return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(RestaurantCalendarDTO.fromEntity(newRestaurant));
	}

	@PutMapping("/{id}")
	public ResponseEntity<RestaurantCalendarDTO> update(@PathVariable Long id, @Valid @RequestBody RestaurantCalendarDTO restaurantDTO){
		//Updating restaurant
		var updateRestaurant = RestaurantDTO.fromDTO(restaurantDTO);
		var restaurant = RestaurantDTO.toEntity(restaurantService.update(id, updateRestaurant));

		//Updating restaurant calendar
		var updateCalendar = calendarService.update(restaurantDTO.calendar().id(), restaurantDTO.calendar());

		//Constructing response return with saved data
		restaurant.setCalendar(CalendarDTO.toEntity(updateCalendar));

		return ResponseEntity.ok(RestaurantCalendarDTO.fromEntity(restaurant));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id){
		restaurantService.delete(id);
		return ResponseEntity.ok("Restaurant " + id + " deleted");
	}
}
