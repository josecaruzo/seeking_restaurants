package com.fiap.seeking_restaurants_provider.service.validation.Restaurant;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RestaurantCreationValidator.class, RestaurantCalendarCreationValidator.class})
public @interface ValidRestaurantCreation {
	String message() default "Restaurant validation error";

	Class<?>[] groups() default {};
	Class<? extends Payload> [] payload() default {};
}
