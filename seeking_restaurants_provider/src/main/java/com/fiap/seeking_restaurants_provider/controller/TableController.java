package com.fiap.seeking_restaurants_provider.controller;

import com.fiap.seeking_restaurants_provider.dto.Table.TableDTO;
import com.fiap.seeking_restaurants_provider.dto.Table.TableRestaurantDTO;
import com.fiap.seeking_restaurants_provider.service.TableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/tables")
public class TableController {
	@Autowired
	private TableService tableService;

	@GetMapping("/{restaurant_id}")
	public ResponseEntity<Collection<TableRestaurantDTO>> findByRestaurant(@PathVariable Long restaurant_id){
		var tables = tableService.findByRestaurant(restaurant_id);
		return ResponseEntity.ok(tables);
	}
	@PostMapping
	public ResponseEntity<TableRestaurantDTO> add( @Valid @RequestBody TableRestaurantDTO tableDTO){
		var newTable = tableService.add(tableDTO);
		return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(newTable);
	}

	@PostMapping("/all/{restaurant_id}")
	public ResponseEntity<Collection<TableRestaurantDTO>> addAll(@PathVariable Long restaurant_id){
		var newTables = tableService.addAll(restaurant_id);
		return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(newTables);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TableDTO> update(@PathVariable Long id, @Valid @RequestBody TableDTO tableDTO){
		TableDTO updateTable = tableService.update(id, tableDTO);
		return ResponseEntity.ok(updateTable);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		tableService.delete(id);
		return ResponseEntity.noContent().build();
	}
}