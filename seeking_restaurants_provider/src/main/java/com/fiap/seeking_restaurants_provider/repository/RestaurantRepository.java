package com.fiap.seeking_restaurants_provider.repository;

import com.fiap.seeking_restaurants_provider.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

	Optional<Restaurant> findByName(String name);
	List<Restaurant> findByNameContainingIgnoreCase(String name);
	List<Restaurant> findByAddressContainingIgnoreCase(String address);
	List<Restaurant> findByTypeIgnoreCase(String type);
}
