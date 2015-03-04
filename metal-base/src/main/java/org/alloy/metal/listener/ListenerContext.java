package org.alloy.metal.listener;

import java.util.function.Consumer;

import org.alloy.metal.collections.map.MutableMultimap;
import org.alloy.metal.collections.map._Maps;

public class ListenerContext<T, N> {
	private MutableMultimap<T, Consumer<N>> internalMap = _Maps.multiMap();

	public void apply(T key, N value) {
		internalMap.get(key).forEach((element) -> element.accept(value));
	}

	public void put(T key, Consumer<N> value) {
		internalMap.put(key, value);
	}
}