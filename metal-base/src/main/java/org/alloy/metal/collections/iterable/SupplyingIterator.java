package org.alloy.metal.collections.iterable;

import java.util.NoSuchElementException;

import org.alloy.metal.collections.flow.Flow;

public class SupplyingIterator<T> extends SingleEntryIterator<T> {
	private Flow<T> flow;

	public SupplyingIterator(Flow<T> flow) {
		this.flow = flow;
	}

	@Override
	protected T fetch() throws NoSuchElementException {
		// TODO
		return null;
//		return flow.forEach(consumer)
	}
}