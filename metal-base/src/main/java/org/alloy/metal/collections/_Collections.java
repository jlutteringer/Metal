package org.alloy.metal.collections;

import java.util.Collection;

import org.alloy.metal.function.equality.Equalitor;

public class _Collections {
	public static <T> ListenableCollection<T> listenableCollection(Collection<T> target) {
		ListenableCollection<T> listenableCollection = new ListenableCollection<>(target);
		return listenableCollection;
	}

	public static <T> CollectionMonitor<T> monitor(Collection<T> collection) {
		return new CollectionMonitor<T>(collection);
	}

	public static <T, N> boolean compareElements(Collection<T> first, Collection<N> second, Equalitor<T, N> equalitor, boolean orderMatters) {
		if (orderMatters) {
			// FUTURE
			throw new RuntimeException();
		}
		else {
			boolean firstCompare = compareOrderless(first, second, equalitor);
			if (!firstCompare) {
				return false;
			}

			boolean secondCompare = compareOrderless(second, first, equalitor.reverse());
			return secondCompare;
		}
	}

	private static <T, N> boolean compareOrderless(Collection<T> first, Collection<N> second, Equalitor<T, N> equalitor) {
		for (T item : first) {
			boolean match = false;
			for (N item2 : second) {
				if (equalitor.apply(item, item2)) {
					match = true;
				}
			}

			if (!match) {
				return false;
			}
		}

		return true;
	}
}