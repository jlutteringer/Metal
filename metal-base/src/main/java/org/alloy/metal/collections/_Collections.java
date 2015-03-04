package org.alloy.metal.collections;

import java.util.Collection;
import java.util.Set;

import org.alloy.metal.equality.Equalitor;

import com.google.common.collect.Sets;

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

	private static <T, N> boolean compareOrderless(Collection<T> targetCollection, Collection<N> containingCollection, Equalitor<T, N> equalitor) {
		Set<Integer> taggedIndicies = Sets.newHashSet();

		for (T targetItem : targetCollection) {
			boolean match = false;

			int count = 0;
			for (N potentialMatch : containingCollection) {
				if (!taggedIndicies.contains(count) && equalitor.apply(targetItem, potentialMatch)) {
					match = true;
					taggedIndicies.add(count);
				}

				count++;
			}

			if (!match) {
				return false;
			}
		}

		return true;
	}
}