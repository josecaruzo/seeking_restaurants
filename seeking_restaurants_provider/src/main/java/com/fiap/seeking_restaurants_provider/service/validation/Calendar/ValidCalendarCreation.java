package com.fiap.seeking_restaurants_provider.service.validation.Calendar;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CalendarCreationValidator.class, CalendarRestaurantCreationValidator.class} )
public @interface ValidCalendarCreation {
	String message() default "Calendar validation error";

	Class<?>[] groups() default {};
	Class<? extends Payload> [] payload() default {};
}
