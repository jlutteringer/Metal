package org.alloy.metal.presentation;

import org.alloy.metal.domain.WithToString;

public class PresentationDescriptionField extends WithToString {
	private String fieldName;
	private Class<?> fieldType;

	public PresentationDescriptionField(String fieldName, Class<?> fieldType) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}
}