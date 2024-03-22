package com.fiap.seeking_restaurants_provider.controller.exception;

public class ValidateMessage {
	private String entity;
	private String field;
	private String message;

	public ValidateMessage(){}

	public ValidateMessage(String entity, String field, String message) {
		this.entity = entity;
		this.field = field;
		this.message = message;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
