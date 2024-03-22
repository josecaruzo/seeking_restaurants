package com.fiap.seeking_restaurants_provider.entity;

import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarDTO;
import com.fiap.seeking_restaurants_provider.dto.Calendar.CalendarRestaurantDTO;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "calendars")
public class Calendar {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * All days working hours will be defined by String using the format hh:mm-hh:mm
	 * Considering the first hh:mm as opening hour and the second one as closing hour.
	 * hh as 24h format
	 * Closed days should be filled with "Closed" or "Fechado"
	 * */

	@Column(length = 11, nullable = false)
	private String monday_hours;
	@Column(length = 11, nullable = false)
	private String tuesday_hours;
	@Column(length = 11, nullable = false)
	private String wednesday_hours;
	@Column(length = 11, nullable = false)
	private String thursday_hours;
	@Column(length = 11, nullable = false)
	private String friday_hours;
	@Column(length = 11, nullable = false)
	private String saturday_hours;
	@Column(length = 11, nullable = false)
	private String sunday_hours;
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	public Calendar(){}

	public Calendar (String monday_hours, String tuesday_hours, String wednesday_hours, String thursday_hours, String friday_hours, String saturday_hours, String sunday_hours){
		this.monday_hours = monday_hours;
		this.tuesday_hours = tuesday_hours;
		this.wednesday_hours = wednesday_hours;
		this.thursday_hours = thursday_hours;
		this.friday_hours = friday_hours;
		this.saturday_hours = saturday_hours;
		this.sunday_hours = sunday_hours;
	}

	public Calendar (CalendarDTO calendarDTO){
		this.id = calendarDTO.id();
		this.monday_hours = calendarDTO.monday_hours();
		this.tuesday_hours = calendarDTO.tuesday_hours();
		this.wednesday_hours = calendarDTO.wednesday_hours();
		this.thursday_hours = calendarDTO.thursday_hours();
		this.friday_hours = calendarDTO.friday_hours();
		this.saturday_hours = calendarDTO.saturday_hours();
		this.sunday_hours = calendarDTO.sunday_hours();
	}

	public Calendar (CalendarRestaurantDTO calendarDTO, Restaurant restaurant){
		this.id = calendarDTO.id();
		this.monday_hours = calendarDTO.monday_hours();
		this.tuesday_hours = calendarDTO.tuesday_hours();
		this.wednesday_hours = calendarDTO.wednesday_hours();
		this.thursday_hours = calendarDTO.thursday_hours();
		this.friday_hours = calendarDTO.friday_hours();
		this.saturday_hours = calendarDTO.saturday_hours();
		this.sunday_hours = calendarDTO.sunday_hours();
		this.restaurant = restaurant;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMonday_hours() {
		return monday_hours;
	}

	public void setMonday_hours(String monday_hours) {
		this.monday_hours = monday_hours;
	}

	public String getTuesday_hours() {
		return tuesday_hours;
	}

	public void setTuesday_hours(String tuesday_hours) {
		this.tuesday_hours = tuesday_hours;
	}

	public String getWednesday_hours() {
		return wednesday_hours;
	}

	public void setWednesday_hours(String wednesday_hours) {
		this.wednesday_hours = wednesday_hours;
	}

	public String getThursday_hours() {
		return thursday_hours;
	}

	public void setThursday_hours(String thursday_hours) {
		this.thursday_hours = thursday_hours;
	}

	public String getFriday_hours() {
		return friday_hours;
	}

	public void setFriday_hours(String friday_hours) {
		this.friday_hours = friday_hours;
	}

	public String getSaturday_hours() {
		return saturday_hours;
	}

	public void setSaturday_hours(String saturday_hours) {
		this.saturday_hours = saturday_hours;
	}

	public String getSunday_hours() {
		return sunday_hours;
	}

	public void setSunday_hours(String sunday_hours) {
		this.sunday_hours = sunday_hours;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "Calendar{" +
				"id=" + id +
				", monday_hours='" + monday_hours + '\'' +
				", tuesday_hours='" + tuesday_hours + '\'' +
				", wednesday_hours='" + wednesday_hours + '\'' +
				", thursday_hours='" + thursday_hours + '\'' +
				", friday_hours='" + friday_hours + '\'' +
				", saturday_hours='" + saturday_hours + '\'' +
				", sunday_hours='" + sunday_hours + '\'' +
				", restaurant=" + restaurant +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Calendar calendar = (Calendar) o;
		return Objects.equals(id, calendar.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
