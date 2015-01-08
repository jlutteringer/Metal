package org.alloy.metal.utilities;

import java.util.function.Predicate;

import org.alloy.metal.collections.iterable._Iterable;
import org.alloy.metal.collections.lists._Lists;
import org.alloy.metal.function._Predicate;

import com.google.common.collect.Iterables;

public class _Precondition {
	@SafeVarargs
	public static <T> void check(Predicate<T> filter, T... objects) {
		if (!Iterables.isEmpty(_Iterable.filter(_Lists.list(objects), filter))) {
			throw new IllegalArgumentException();
		}
	}

	@SafeVarargs
	public static <T> void notNull(T... objects) {
		_Precondition.check(_Predicate.isDefined().negate(), objects);
	}
}