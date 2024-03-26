package com.fiap.seeking_restaurants_provider.entity;

import com.fiap.seeking_restaurants_provider.dto.Review.ReviewDTO;
import com.fiap.seeking_restaurants_provider.dto.Review.ReviewRestaurantDTO;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "reviews")
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 30, nullable = false)
	private String guestName;

	@Column(scale = 1, nullable = false)
	private int stars; // number between 1 and 5

	@Column(length = 200)
	private String comment; //review comment is optional

	@ManyToOne
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	public Review(){}

	public Review(Long id, String guestName, int stars, String comment){
		this.id = id;
		this.guestName = guestName;
		this.stars = stars;
		this.comment = comment;
	}

	public Review(ReviewDTO reviewDTO) {
		this.id = reviewDTO.id();
		this.guestName = reviewDTO.guestName();
		this.stars = reviewDTO.stars();
		this.comment = reviewDTO.comment();
	}

	public Review(ReviewRestaurantDTO reviewDTO, Restaurant restaurant) {
		this.id = reviewDTO.id();
		this.guestName = reviewDTO.guestName();
		this.stars = reviewDTO.stars();
		this.comment = reviewDTO.comment();
		this.restaurant = restaurant;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "Review{" +
				"id=" + id +
				", guestName='" + guestName + '\'' +
				", stars=" + stars +
				", comment='" + comment + '\'' +
				", restaurant=" + restaurant +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Review review = (Review) o;
		return Objects.equals(id, review.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
