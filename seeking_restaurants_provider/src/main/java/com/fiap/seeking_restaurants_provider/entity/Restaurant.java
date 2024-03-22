package com.fiap.seeking_restaurants_provider.entity;

import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantCalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Restaurant.RestaurantDTO;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@jakarta.persistence.Table(name = "restaurants")
public class Restaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	@Column(length = 8, nullable = false)
	private String zipcode;

	@Column(length = 100, nullable = false)
	private String address;

	@Column(length = 20, nullable = false)
	private String type; //ex.: Italian, Japanese, Portuguese, Brazilian, Street Food, etc.

	@Column(nullable = false)
	private int capacity; // number in tables capacity, considering each table with 4 guests capacity

	@OneToOne(mappedBy = "restaurant", cascade = CascadeType.REMOVE)
	private Calendar calendar;

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE)
	Set<Review> reviews = new HashSet<>();;

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE)
	Set<Table> tables = new HashSet<>();;

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE)
	Set<Reservation> reservations = new HashSet<>();

	public Restaurant(){}

	public Restaurant(Long id, String name, String zipcode, String address, String type, int capacity){
		this.id = id;
		this.name = name;
		this.zipcode = zipcode;
		this.address = address;
		this.type = type;
		this.capacity = capacity;
	}

	public Restaurant(RestaurantDTO restaurantDTO) {
		this.id = restaurantDTO.id();
		this.name = restaurantDTO.name();
		this.zipcode = restaurantDTO.zipcode();
		this.address = restaurantDTO.address();
		this.type = restaurantDTO.type();
		this.capacity = restaurantDTO.capacity();
	}

	public Restaurant(RestaurantCalendarDTO restaurantDTO) {
		this.id = restaurantDTO.id();
		this.name = restaurantDTO.name();
		this.zipcode = restaurantDTO.zipcode();
		this.address = restaurantDTO.address();
		this.type = restaurantDTO.type();
		this.capacity = restaurantDTO.capacity();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {return type;}

	public void setType(String type) {this.type = type;}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	@Override
	public String toString() {
		return "Restaurant{" +
				"id=" + id +
				", name='" + name + '\'' +
				", zipcode='" + zipcode + '\'' +
				", address='" + address + '\'' +
				", type='" + type + '\'' +
				", capacity=" + capacity +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Restaurant that = (Restaurant) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
