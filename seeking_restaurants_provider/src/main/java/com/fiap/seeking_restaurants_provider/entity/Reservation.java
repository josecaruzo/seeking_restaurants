package com.fiap.seeking_restaurants_provider.entity;

import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationDTO;
import com.fiap.seeking_restaurants_provider.dto.Reservation.ReservationRestaurantDTO;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@jakarta.persistence.Table(name = "reservations")
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@DateTimeFormat(pattern="dd/MM/YYYY")
	private LocalDate reservationDate;

	@Column(nullable = false)
	@DateTimeFormat(pattern="HH:mm")
	private LocalTime reservationHour;

	@Column(length = 40, nullable = false)
	private String guestName;

	@Column(length = 80, nullable = false)
	private String guestEmail;

	@Column(length = 12, nullable = false)
	private String guestPhone;

	@Column(nullable = false)
	private int totalTables;

	@ManyToMany
	private Set<Table> tables = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	public Reservation(){}

	public Reservation(LocalDate reservationDate, LocalTime reservationHour, String guestName, String guestEmail, String guestPhone, int totalTables){
		this.reservationDate = reservationDate;
		this.reservationHour = reservationHour;
		this.guestName = guestName;
		this.guestEmail = guestEmail;
		this.guestPhone = guestPhone;
		this.totalTables = totalTables;
	}
	public Reservation(ReservationDTO reservationDTO){
		this.id = reservationDTO.id();
		this.reservationDate = reservationDTO.reservationDate();
		this.reservationHour = reservationDTO.reservationHour();
		this.guestName = reservationDTO.guestName();
		this.guestEmail = reservationDTO.guestEmail();
		this.guestPhone = reservationDTO.guestPhone();
		this.totalTables = reservationDTO.totalTables();
	}

	public Reservation(ReservationRestaurantDTO reservationDTO, Restaurant restaurant) {
		this.id = reservationDTO.id();
		this.reservationDate = reservationDTO.reservationDate();
		this.reservationHour = reservationDTO.reservationHour();
		this.guestName = reservationDTO.guestName();
		this.guestEmail = reservationDTO.guestEmail();
		this.guestPhone = reservationDTO.guestPhone();
		this.totalTables = reservationDTO.totalTables();
		this.restaurant = restaurant;
	}

	public Reservation(ReservationRestaurantDTO reservationDTO, Restaurant restaurant, Set<Table> tables) {
		this.id = reservationDTO.id();
		this.reservationDate = reservationDTO.reservationDate();
		this.reservationHour = reservationDTO.reservationHour();
		this.guestName = reservationDTO.guestName();
		this.guestEmail = reservationDTO.guestEmail();
		this.guestPhone = reservationDTO.guestPhone();
		this.totalTables = reservationDTO.totalTables();
		this.restaurant = restaurant;
		this.tables = tables;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}

	public LocalTime getReservationHour() {
		return reservationHour;
	}

	public void setReservationHour(LocalTime reservationHour) {
		this.reservationHour = reservationHour;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getGuestEmail() {
		return guestEmail;
	}

	public void setGuestEmail(String guestEmail) {
		this.guestEmail = guestEmail;
	}

	public String getGuestPhone() {
		return guestPhone;
	}

	public void setGuestPhone(String guestPhone) {
		this.guestPhone = guestPhone;
	}

	public int getTotalTables() {
		return totalTables;
	}

	public void setTotalTables(int totalTables) {
		this.totalTables = totalTables;
	}

	public Set<Table> getTables() {
		return tables;
	}

	public void setTables(Set<Table> tables) {
		this.tables = tables;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "Reservation{" +
				"id=" + id +
				", reservationDate=" + reservationDate +
				", reservationHour=" + reservationHour +
				", guestName='" + guestName + '\'' +
				", guestEmail='" + guestEmail + '\'' +
				", guestPhone='" + guestPhone + '\'' +
				", tables=" + tables +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Reservation that = (Reservation) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
