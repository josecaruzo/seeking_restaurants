package com.fiap.seeking_restaurants_provider.controller;

import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantTableDTO;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantCalendarDTO;
import com.fiap.seeking_restaurants_provider.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
	@Autowired
	private ReservationService reservationService;

	@GetMapping("/restaurant/{restaurant_id}")
	public ResponseEntity<Collection<ReservationRestaurantTableDTO>> findByRestaurant(@PathVariable Long restaurant_id){
		var reservations = reservationService.findByRestaurant(restaurant_id);
		return ResponseEntity.ok(reservations);
	}

	@GetMapping("/restaurant/{restaurant_id}/date/{reservationDate}")
	public ResponseEntity<Collection<ReservationRestaurantTableDTO>> findByRestaurantAndReservationDate(@PathVariable Long restaurant_id, @PathVariable LocalDate reservationDate){
		var reservations = reservationService.findByRestaurantAndReservationDate(reservationDate, restaurant_id);
		return ResponseEntity.ok(reservations);
	}

	@GetMapping("/restaurant/{restaurant_id}/date/{reservationDate}/hour/{reservationHour}")
	public ResponseEntity<Collection<ReservationRestaurantTableDTO>> findByRestaurantAndReservationDateAndReservationHour(
			@PathVariable Long restaurant_id, @PathVariable LocalDate reservationDate, @PathVariable LocalTime reservationHour){
		var reservations = reservationService.findByRestaurantAndReservationDateAndReservationHour(restaurant_id, reservationDate, reservationHour);
		return ResponseEntity.ok(reservations);
	}
	@PostMapping
	public ResponseEntity<ReservationRestaurantTableDTO> add(@RequestBody ReservationRestaurantDTO reservationDTO){
		var newReservation = reservationService.add(reservationDTO);
		return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(newReservation);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ReservationRestaurantTableDTO> update(@PathVariable Long id, @RequestBody ReservationRestaurantDTO reservationDTO){
		var updateReservation = reservationService.update(id,reservationDTO);
		return  ResponseEntity.ok(updateReservation);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		reservationService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
