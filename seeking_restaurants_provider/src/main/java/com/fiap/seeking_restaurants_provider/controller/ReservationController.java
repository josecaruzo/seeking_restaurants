package com.fiap.seeking_restaurants_provider.controller;

import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantTableDTO;
import com.fiap.seeking_restaurants_provider.service.ReservationService;
import jakarta.validation.Valid;
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
	private final ReservationService reservationService;

	@Autowired
	public ReservationController(ReservationService reservationService){
		this.reservationService = reservationService;
	}

	@GetMapping("/restaurant/{restaurantId}")
	public ResponseEntity<Collection<ReservationRestaurantTableDTO>> findByRestaurant(@PathVariable Long restaurantId){
		var reservations = reservationService.findByRestaurant(restaurantId);
		return ResponseEntity.ok(reservations);
	}

	@GetMapping("/restaurant/{restaurantId}/date/{reservationDate}")
	public ResponseEntity<Collection<ReservationRestaurantTableDTO>> findByRestaurantAndReservationDate(@PathVariable Long restaurantId, @PathVariable LocalDate reservationDate){
		var reservations = reservationService.findByRestaurantAndReservationDate(restaurantId, reservationDate);
		return ResponseEntity.ok(reservations);
	}

	@GetMapping("/restaurant/{restaurantId}/date/{reservationDate}/hour/{reservationHour}")
	public ResponseEntity<Collection<ReservationRestaurantTableDTO>> findByRestaurantAndReservationDateAndReservationHour(
			@PathVariable Long restaurantId, @PathVariable LocalDate reservationDate, @PathVariable LocalTime reservationHour){
		var reservations = reservationService.findByRestaurantAndReservationDateAndReservationHour(restaurantId, reservationDate, reservationHour);
		return ResponseEntity.ok(reservations);
	}
	@PostMapping
	public ResponseEntity<ReservationRestaurantTableDTO> add(@Valid @RequestBody ReservationRestaurantDTO reservationDTO){
		var newReservation = reservationService.add(reservationDTO);
		return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(newReservation);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ReservationRestaurantTableDTO> update(@PathVariable Long id, @Valid @RequestBody ReservationRestaurantDTO reservationDTO){
		var updateReservation = reservationService.update(id,reservationDTO);
		return  ResponseEntity.ok(updateReservation);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id){
		reservationService.delete(id);
		return ResponseEntity.ok("Reservation " + id + " deleted");
	}
}
