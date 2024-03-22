package com.fiap.seeking_restaurants_provider.controller.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidateError extends StandardError{
	private List<ValidateMessage> messages = new ArrayList<ValidateMessage>();

	public List<ValidateMessage> getMessages() {
		return messages;
	}

	public void addMessage(String entity, String field, String message){
		messages.add(new ValidateMessage(entity, field,message));
	}


}
