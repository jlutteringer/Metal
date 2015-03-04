package org.alloy.metal.object;

import java.util.Map;

import org.alloy.metal.collections.map._Maps;
import org.alloy.metal.iteration._Iteration;

public class _Domain {
	public static <T extends Named> Map<String, T> mapNames(Iterable<T> iterable) {
		return _Maps.map((element) -> element.getName(), _Iteration.cursor(iterable)).asMap();
	}

	public static <T extends FriendlyNamed> Map<String, T> mapFriendlyNames(Iterable<T> iterable) {
		return _Maps.map((element) -> element.getFriendlyName(), _Iteration.cursor(iterable)).asMap();
	}

	public static void copy(Named target, Named source) {
		target.setName(source.getName());
	}

	public static void copy(FriendlyNamed target, FriendlyNamed source) {
		copy((Named) target, (Named) source);
		target.setFriendlyName(source.getFriendlyName());
	}
}