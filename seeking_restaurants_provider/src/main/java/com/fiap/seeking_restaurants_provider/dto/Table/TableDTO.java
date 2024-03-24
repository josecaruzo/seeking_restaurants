package com.fiap.seeking_restaurants_provider.dto.Table;

import com.fiap.seeking_restaurants_provider.entity.Table;
import jakarta.validation.constraints.Positive;

public record TableDTO(
		Long id,

		@Positive(message = "O número da mesa precisa ser positivo") // The table's number should be greater than 0
		int number,

		@Positive(message = "A capacidade da mesa precisa ser positivo") // The table's capacity should be greater than 0
		int capacity
) {
	public static Table toEntity(TableDTO tableDTO) {
		return new Table(tableDTO);
	}

	public static TableDTO fromEntity(Table table) {
		return  new TableDTO(
				table.getId(),
				table.getNumber(),
				table.getCapacity()
		);
	}

	public static Table mapperDtoToEntity(TableDTO dto, Table entity) {
		entity.setNumber(dto.number());
		entity.setCapacity(dto.capacity());
		return entity;
	}
}
