package org.alloy.metal.collections;

import java.util.Collection;

import org.alloy.metal.equality._Equality;
import org.alloy.metal.utility.Condition;

import com.google.common.collect.Lists;

public class CollectionMonitor<T> {
	private Collection<T> collectionToMonitor;

	public CollectionMonitor(Collection<T> collectionToMonitor) {
		this.collectionToMonitor = collectionToMonitor;
	}

	public Condition hasChanged() {
		return new Condition() {
			private Collection<T> previousCollection = Lists.newArrayList();

			@Override
			public boolean test() {
				boolean hasChanged = true;
				if (previousCollection.size() != collectionToMonitor.size()) {
					hasChanged = true;
				}
				else if (_Collections.compareElements(previousCollection, collectionToMonitor, _Equality.natural(), false)) {
					hasChanged = false;
				}

				previousCollection = Lists.newArrayList(collectionToMonitor);
				return hasChanged;
			}
		};
	}
}