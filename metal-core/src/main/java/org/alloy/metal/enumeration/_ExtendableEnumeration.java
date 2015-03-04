package org.alloy.metal.enumeration;

import org.alloy.metal.collections.list.MutableList;
import org.alloy.metal.collections.list._Lists;
import org.alloy.metal.collections.map.MutableMap;
import org.alloy.metal.collections.map._Maps;

import com.google.common.base.Throwables;

public class _ExtendableEnumeration {
	private static volatile boolean configured = false;
	private static MutableMap<String, MutableList<ExtendableEnumeration>> enumerations = _Maps.defaultMap(() -> _Lists.list());

	public static boolean isConfigured() {
		return configured;
	}

	public static void endConfiguration() {
		configured = true;
	}

	public static <T extends AbstractExtendableEnumeration> T create(String type, String friendlyType, Class<T> clazz) {
		if (configured) {
			throw new RuntimeException("Configuration of enumerations has already been completed,"
					+ " cannot create " + type + " of class " + clazz);
		}

		try {
			T newEnumeration = clazz.newInstance();
			newEnumeration.setName(type);
			newEnumeration.setFriendlyName(friendlyType);
			enumerations.get(type).add(newEnumeration);

			return newEnumeration;
		} catch (InstantiationException | IllegalAccessException e) {
			throw Throwables.propagate(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends ExtendableEnumeration> T getInstance(String type, Class<T> clazz) {
		if (!configured) {
			throw new RuntimeException("Configuration of enumerations has not been completed");
		}

		if (enumerations.containsKey(type)) {
			return (T) enumerations.get(type)
					.filter((enumeration) -> clazz.isAssignableFrom(enumeration.getClass()))
					.single()
					.orElseThrow(() -> new RuntimeException("Error finding matching enumeration for type: " + type + " and class " + clazz));
		}

		throw new RuntimeException("No matching enumeration for type: " + type + " and class " + clazz);
	}
}