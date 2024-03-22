package com.fiap.seeking_restaurants_provider.entity;

import com.fiap.seeking_restaurants_provider.dto.Table.TableDTO;
import com.fiap.seeking_restaurants_provider.dto.Table.TableRestaurantDTO;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@jakarta.persistence.Table(name = "tables")
public class Table { //Restable = Restaurant table
	/** id is different from table number considering each restaurant can have table 01 for example
	 * using only id to identify table would private restaurant to choose their own table number
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int number;

	@Column(scale = 2, nullable = false)
	private int capacity; // by default capacity will be 4 (but it could be changed at maximum 99)

	@ManyToOne
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@ManyToMany
	private Set<Reservation> reservations = new HashSet<>();

	public Table(){}

	public Table(int number, int capacity){
		this.number = number;
		this.capacity = capacity;
	}

	public Table(TableDTO tableDTO){
		this.id = tableDTO.id();
		this.number = tableDTO.number();
		this.capacity = tableDTO.capacity();
	}

	public Table(TableRestaurantDTO tableDTO, Restaurant restaurant){
		this.id = tableDTO.id();
		this.number = tableDTO.number();
		this.capacity = tableDTO.capacity();
		this.restaurant = restaurant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "Table{" +
				"id=" + id +
				", number=" + number +
				", capacity=" + capacity +
				", restaurant=" + restaurant +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Table table = (Table) o;
		return Objects.equals(id, table.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
