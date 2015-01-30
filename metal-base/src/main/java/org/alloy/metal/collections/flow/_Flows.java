package org.alloy.metal.collections.flow;

import java.util.Iterator;

import org.alloy.metal.collections.iterable.SupplyingIterator;

public class _Flows {
	public static <T> Flow<T> flow(Iterable<T> iterable) {
		return flow(iterable.iterator());
	}

	public static <T> Flow<T> flow(Iterator<T> iterator) {
		return (consumer) -> {
			boolean cont = true;
			while (iterator.hasNext() && cont) {
				cont = consumer.apply(iterator.next());
			}

			return (callback) -> callback.apply();
		};
	}

	public static <T> Iterator<T> iterator(Flow<T> flow) {
		return new SupplyingIterator<T>(flow);
	}

	public static <T> Flow<T> empty() {
		// TODO Auto-generated method stub
		return null;
	}
}