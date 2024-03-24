package com.fiap.seeking_restaurants_provider.dto.Reservation;

import com.fiap.seeking_restaurants_provider.entity.Reservation;
import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import com.fiap.seeking_restaurants_provider.service.validation.Reservation.ValidReservationHour;
import com.fiap.seeking_restaurants_provider.service.validation.Reservation.ValidReservationPhoneNumber;
import com.fiap.seeking_restaurants_provider.service.validation.Reservation.ValidReservationCreation;
import com.fiap.seeking_restaurants_provider.service.validation.Reservation.ValidReservationRestaurantCalendar;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

@ValidReservationPhoneNumber(message= "Telefone inválido, siga o formato de número (99)99999-9999 ou (99)9999-9999")
@ValidReservationHour(message = "A hora da reserva precisa ser em hora 'fechada'. Ex.: 19:00, 17:00")
@ValidReservationCreation(message = "A Data e o horário estão no passado, só é possível fazer reserva para uma data futura")
@ValidReservationRestaurantCalendar(message = "O restaurante não está disponível para reservas nesta data e hora, consulte o horário de funcionamento de estabelcimento")
public record ReservationRestaurantDTO(
		Long id,
		@FutureOrPresent(message = "A data da reserva não pode ser feita no passado") //Reservation date can't be done in the past
		LocalDate reservationDate,
		@NotNull(message = "A hora da reserva não pode ser vazia") //Reservation hour can't be empty
		LocalTime reservationHour,

		@NotBlank(message = "Nome do convidado não pode ser vazio") // Guest name can't be blank
		String guestName,

		@Email(message = "Adicione um email válido") // Add a valid email
		String guestEmail,

		@NotBlank(message = "Telefone não pode ser vazio") // phone can't be blank
		String guestPhone,

		@Positive(message = "O total de mesas para reserva deve ser maior que zero") // tables can't be negative or zero
		int totalTables,

		Long restaurant_id
){
	public static Reservation toEntity(ReservationRestaurantDTO reservationDTO, Restaurant restaurant) {
		return new Reservation(reservationDTO, restaurant);
	}

	public static ReservationRestaurantDTO fromEntity(Reservation reservation) {
		return  new ReservationRestaurantDTO(
				reservation.getId(),
				reservation.getReservationDate(),
				reservation.getReservationHour(),
				reservation.getGuestName(),
				reservation.getGuestEmail(),
				reservation.getGuestPhone(),
				reservation.getTotalTables(),
				reservation.getRestaurant() != null ? reservation.getRestaurant().getId() : null
		);
	}

	public static Reservation mapperDtoToEntity(ReservationRestaurantDTO dto, Reservation entity, Restaurant restaurant) {
		entity.setReservationDate(dto.reservationDate());
		entity.setReservationHour(dto.reservationHour());
		entity.setGuestName(dto.guestName());
		entity.setGuestEmail(dto.guestEmail());
		entity.setGuestPhone(dto.guestPhone());
		entity.setTotalTables(dto.totalTables());
		entity.setRestaurant(restaurant);
		return entity;
	}
}
