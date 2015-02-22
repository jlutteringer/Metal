package org.alloy.metal.collections.iterable;

import java.util.NoSuchElementException;

import org.alloy.metal.function.Value;
import org.alloy.metal.function.StatefulSupplier;

public class GeneratingIterator<T, N> extends SingleEntryIterator<T> {
	private StatefulSupplier<N, Value<T>> supplier;
	private N state;

	public GeneratingIterator(StatefulSupplier<N, Value<T>> supplier, N state) {
		this.supplier = supplier;
		this.state = state;
	}

	@Override
	protected T fetch() throws NoSuchElementException {
		return supplier.apply(state).getOrThrow(new NoSuchElementException());
	}
}