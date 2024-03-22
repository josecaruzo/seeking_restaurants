package com.fiap.seeking_restaurants_provider.controller.exception;

public class ControllerNotFoundException extends RuntimeException{

	public ControllerNotFoundException(String message){
		super(message);
	}
}
