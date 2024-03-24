package com.fiap.seeking_restaurants_provider.controller.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ControllerExceptionHandler {

	private final StandardError newError = new StandardError();

	@ExceptionHandler(ControllerNotFoundException.class)
	public ResponseEntity<StandardError> controllerNotFound(ControllerNotFoundException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.NOT_FOUND;

		newError.setTimestamp(Instant.now());
		newError.setStatus(status.value());
		newError.setError("Controller not found.");
		newError.setMessage(e.getMessage());
		newError.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(newError);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;

		ValidateError newValidError = new ValidateError();

		newValidError.setTimestamp(Instant.now());
		newValidError.setStatus(status.value());
		newValidError.setError("Entity not valid.");
		newValidError.setMessage(e.getMessage());
		newValidError.setPath(request.getRequestURI());

		for(FieldError f: e.getBindingResult().getFieldErrors()){
			newValidError.addMessage(f.getObjectName(), f.getField(),f.getDefaultMessage());
		}

		for(ObjectError g: e.getBindingResult().getGlobalErrors()){
			newValidError.addMessage(g.getObjectName(), "Global", g.getDefaultMessage());
		}

		return ResponseEntity.status(status).body(newValidError);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound (EntityNotFoundException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.NOT_FOUND;

		newError.setTimestamp(Instant.now());
		newError.setStatus(status.value());
		newError.setError("Entity not found.");
		newError.setMessage(e.getMessage());
		newError.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(newError);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> databaseException (DatabaseException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;

		newError.setTimestamp(Instant.now());
		newError.setStatus(status.value());
		newError.setError("Database exception.");
		newError.setMessage(e.getMessage());
		newError.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(newError);
	}

	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<StandardError> dateTimeException (DateTimeParseException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;

		newError.setTimestamp(Instant.now());
		newError.setStatus(status.value());
		newError.setError("DateTime exception");
		newError.setMessage(e.getMessage());
		newError.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(newError);
	}
}
