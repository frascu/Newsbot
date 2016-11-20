package com.frascu.bot.newsbot.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class YesNoConverterToBoolean implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		return attribute ? "Y" : "N";
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		return "Y".equals(dbData);
	}

}