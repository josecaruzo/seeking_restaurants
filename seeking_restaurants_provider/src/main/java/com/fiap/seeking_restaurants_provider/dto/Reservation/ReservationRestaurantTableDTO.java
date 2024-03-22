package com.fiap.seeking_restaurants_provider.dto.Reservation;

		import com.fiap.seeking_restaurants_provider.entity.Reservation;
		import com.fiap.seeking_restaurants_provider.entity.Restaurant;
		import com.fiap.seeking_restaurants_provider.entity.Table;
		import jakarta.validation.constraints.Email;
		import jakarta.validation.constraints.FutureOrPresent;
		import jakarta.validation.constraints.Min;
		import jakarta.validation.constraints.NotBlank;

		import java.time.LocalDate;
		import java.time.LocalTime;
		import java.util.Set;
		import java.util.stream.Collectors;

public record ReservationRestaurantTableDTO(
		Long id,
		@FutureOrPresent(message = "A data da reserva não pode ser feita no passado") //Reservation date can't be done in the past
		LocalDate reservationDate,
		@FutureOrPresent(message = "A hora da reserva não pode ser em um horário no passado") //Reservation hour can't be done in the past
		LocalTime reservationHour,

		@NotBlank(message = "Nome do convidado não pode ser vazio") // Guest name can't be blank
		String guestName,

		@Email(message = "Adicione um email válido") // Add a valid email
		String guestEmail,

		@NotBlank(message = "Telefone não pode ser vazio") // phone can't be blank
		String guestPhone,

		@Min(value = 1, message = "Telefone não pode ser vazio") // phone can't be blan
		int totalTables,

		Long restaurant_id,

		Set<Integer> table_numbers
){
	public static Reservation toEntity(ReservationRestaurantDTO reservationDTO, Restaurant restaurant, Set<Table> tables) {
		return new Reservation(reservationDTO, restaurant, tables);
	}

	public static ReservationRestaurantTableDTO fromEntity(Reservation reservation) {
		return  new ReservationRestaurantTableDTO(
				reservation.getId(),
				reservation.getReservationDate(),
				reservation.getReservationHour(),
				reservation.getGuestName(),
				reservation.getGuestEmail(),
				reservation.getGuestPhone(),
				reservation.getTotalTables(),
				reservation.getRestaurant() != null ? reservation.getRestaurant().getId() : null,
				reservation.getTables() != null ? reservation.getTables().stream().map(Table::getNumber).collect(Collectors.toSet()) : null
		);
	}

	public static Reservation mapperDtoToEntity(ReservationRestaurantDTO dto, Reservation entity, Restaurant restaurant, Set<Table> tables) {
		entity.setReservationDate(dto.reservationDate());
		entity.setReservationHour(dto.reservationHour());
		entity.setGuestName(dto.guestName());
		entity.setGuestEmail(dto.guestEmail());
		entity.setGuestPhone(dto.guestPhone());
		entity.setTotalTables(dto.totalTables());
		entity.setRestaurant(restaurant);
		entity.setTables(tables);
		return entity;
	}
}