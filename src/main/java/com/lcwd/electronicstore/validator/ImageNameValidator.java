package com.lcwd.electronicstore.validator;

import org.slf4j.*;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String>{ 
	
    private Logger logger =LoggerFactory.getLogger(ImageNameValidator.class);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		logger.info("Message from isValid {}", value);
		
		if(value.isBlank()) {
			return false;
		}else {
		return true;
		}
	}
}
