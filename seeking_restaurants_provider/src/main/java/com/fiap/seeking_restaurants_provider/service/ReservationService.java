package com.fiap.seeking_restaurants_provider.service;

import com.fiap.seeking_restaurants_provider.controller.exception.DatabaseException;
import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantTableDTO;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantCalendarDTO;
import com.fiap.seeking_restaurants_provider.entity.Reservation;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.entity.Table;
import com.fiap.seeking_restaurants_provider.repository.ReservationRepository;
import com.fiap.seeking_restaurants_provider.repository.RestaurantRepository;
import com.fiap.seeking_restaurants_provider.repository.TableRepository;
import com.sun.source.tree.WhileLoopTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final RestaurantRepository restaurantRepository;
	private final TableRepository tableRepository;

	@Autowired
	public ReservationService(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository, TableRepository tableRepository){
		this.reservationRepository = reservationRepository;
		this.restaurantRepository = restaurantRepository;
		this.tableRepository = tableRepository;
	}

	public List<Table> getAvailableTables(Reservation reservation, Restaurant restaurant){
		//Get all reservation did to this restaurant / day / hour
		var scheduledReservations = reservationRepository.findByRestaurantAndReservationDateAndReservationHour(
				restaurant,
				reservation.getReservationDate(),
				reservation.getReservationHour()
		);

		//Get all restaurant tables
		var availableTables = tableRepository.findByRestaurant(restaurant);

		// Looks into all reservations did to this restaurant (Considering day / hour) and removing it from restaurant tables
		// It will result in all available tables
		for (Reservation scheduledReservation : scheduledReservations) {
			var tempTables = scheduledReservation.getTables().stream().toList();
			for (Table tempTable : tempTables) {
				availableTables.remove(tempTable);
			}
		}
		return availableTables;
	}

	public List<ReservationRestaurantTableDTO> findByRestaurant(Long restaurant_id){
		try{
			Restaurant restaurant = restaurantRepository.getReferenceById(restaurant_id);
			var reservations = reservationRepository.findByRestaurant(restaurant);
			return reservations.stream().map(ReservationRestaurantTableDTO::fromEntity).collect(Collectors.toList());
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Restaurante não encontrado"); // Restaurant not found
		}
	}

	public List<ReservationRestaurantTableDTO> findByRestaurantAndReservationDate(LocalDate reservationDate, Long restaurant_id){
		try{
			Restaurant restaurant = restaurantRepository.getReferenceById(restaurant_id);
			var reservations = reservationRepository.findByRestaurantAndReservationDate(restaurant, reservationDate);
			return reservations.stream().map(ReservationRestaurantTableDTO::fromEntity).collect(Collectors.toList());
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Restaurante não encontrado"); // Restaurant not found
		}
	}

	public List<ReservationRestaurantTableDTO> findByRestaurantAndReservationDateAndReservationHour(
			Long restaurant_id, LocalDate reservationDate, LocalTime reservationHour){
		try{
			Restaurant restaurant = restaurantRepository.getReferenceById(restaurant_id);
			var reservations = reservationRepository.findByRestaurantAndReservationDateAndReservationHour(restaurant, reservationDate, reservationHour);
			return reservations.stream().map(ReservationRestaurantTableDTO::fromEntity).collect(Collectors.toList());
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Restaurante não encontrado"); // Restaurant not found
		}
	}

	public ReservationRestaurantTableDTO add(ReservationRestaurantDTO dto) {
		try{
			Restaurant restaurant = restaurantRepository.getReferenceById(dto.restaurant_id());
			Reservation reservation = ReservationRestaurantDTO.toEntity(dto, restaurant);
			//Defines empty table list to add reservation tables after table's validation
			Set<Table> tables = new HashSet<Table>();

			var availableTables = getAvailableTables(reservation, restaurant);

			//If total capacity - available tables + all tables that we need to schedule > restaurant total capacity = returns error
			if((restaurant.getCapacity() - availableTables.size()) + reservation.getTotalTables() > restaurant.getCapacity()){
				throw new DatabaseException("Restaurante não tem mesas disponíveis para a reserva");
			}
			else{
				//Adding available tables to the list
				for (int i = 0; i < reservation.getTotalTables(); i ++){
					tables.add(availableTables.get(i));
				}
			}

			//Setting tables into reservation and saving it
			reservation.setTables(tables);
			var newReservation = reservationRepository.save(reservation);

			return ReservationRestaurantTableDTO.fromEntity(newReservation);
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Restaurante não encontrado"); // Restaurant not found
		}
	}

	public ReservationRestaurantTableDTO update(Long id, ReservationRestaurantDTO reservationDTO) {
		try{
			Reservation oldReservation = reservationRepository.getReferenceById(id);
			Restaurant restaurant = oldReservation.getRestaurant();
			Reservation reservation = ReservationRestaurantTableDTO.toEntity(reservationDTO, restaurant, oldReservation.getTables());
			//Defines empty table list to add reservation tables after table's validation
			Set<Table> tables = new HashSet<Table>();
			var availableTables = getAvailableTables(reservation, restaurant);

			if(!reservation.getReservationDate().equals(oldReservation.getReservationDate()) ||
				!reservation.getReservationHour().equals(oldReservation.getReservationHour())){

				//If total capacity - available tables + all tables that we need to schedule > restaurant total capacity = returns error
				if((restaurant.getCapacity() - availableTables.size()) + reservation.getTotalTables() > restaurant.getCapacity()){
					throw new DatabaseException("Restaurante não tem mesas disponíveis para a reserva");
				}
				else{
					//Adding available tables to the list
					for (int i = 0; i < reservation.getTotalTables(); i ++){
						tables.add(availableTables.get(i));
					}
				}
			}
			else{
				if(reservation.getTotalTables() < oldReservation.getTotalTables()){
					List<Table> tempTables =  new LinkedList<Table> (oldReservation.getTables().stream().toList());
					for(int i = 0; i < (oldReservation.getTotalTables() - reservation.getTotalTables()); i++){
						tempTables.removeLast();
					}
					tables = Set.copyOf(tempTables);
				}
				else{
					if(reservation.getTotalTables() > oldReservation.getTotalTables()){
						//If total capacity - available tables + all tables that we need to schedule > restaurant total capacity = returns error
						if((restaurant.getCapacity() - availableTables.size()) + (reservation.getTotalTables() - oldReservation.getTotalTables()) > restaurant.getCapacity()){
							throw new DatabaseException("Restaurante não tem mesas disponíveis para a reserva");
						}
						else{
							List<Table> tempTables = new LinkedList<Table>(oldReservation.getTables().stream().toList());
							//Adding available tables to the list
							for (int i = 0; i < reservation.getTotalTables() - oldReservation.getTotalTables(); i ++){
								tempTables.add(availableTables.get(i));
							}
							tables = Set.copyOf(tempTables);
						}
					}
					else{
						tables = reservation.getTables();
					}
				}
			}

			oldReservation.setReservationDate(reservation.getReservationDate());
			oldReservation.setReservationHour(reservation.getReservationHour());
			oldReservation.setGuestName(reservation.getGuestName());
			oldReservation.setGuestEmail(reservation.getGuestEmail());
			oldReservation.setGuestPhone(reservation.getGuestPhone());
			oldReservation.setTotalTables(reservation.getTotalTables());
			oldReservation.setTables(tables);

			var updatedReservation = reservationRepository.save(oldReservation);

			return ReservationRestaurantTableDTO.fromEntity(updatedReservation);
		}catch ( DataIntegrityViolationException e) {
			throw new DatabaseException("Restaurante não encontrado"); // Restaurant not found
		}
	}

	public void delete(Long id){
		reservationRepository.deleteById(id);
	}

}
