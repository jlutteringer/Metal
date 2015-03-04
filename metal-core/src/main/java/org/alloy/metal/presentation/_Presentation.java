package org.alloy.metal.presentation;

import org.alloy.metal.collections.list.AList;
import org.alloy.metal.collections.list._Lists;
import org.alloy.metal.flow.Flow;
import org.alloy.metal.reflection._Reflection;

public class _Presentation {
	public static PresentationDescription getPresentationDescription(Class<?> clazz) {
		AList<PresentationDescriptionField> descriptionFields = getPresentationDescriptionFields(clazz).collectList();
		System.out.println(descriptionFields);
		return null;
	}

	private static Flow<PresentationDescriptionField> getPresentationDescriptionFields(Class<?> targetClass) {
//		boolean checkFields = true;
//		if (_Reflection.getAnnotation(clazz, Presentation.class).isPresent()) {
//			checkFields = false;
//		}
		return _Reflection.walkClassHierarchy(targetClass).flatMap((clazz) -> {
			Flow<PresentationDescriptionField> flow = _Lists.list(clazz.getDeclaredFields())
					.map((refelctionField) -> new PresentationDescriptionField(refelctionField.getName(), refelctionField.getType()));

			return flow;
		});
	}
}