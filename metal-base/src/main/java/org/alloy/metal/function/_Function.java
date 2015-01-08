package org.alloy.metal.function;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.alloy.metal.collections.iterable._Iterable;

public class _Function {
	public interface Fun0<T> {
		public T apply();
	}

	public interface _Supplier<T> extends Fun0<T> {

	}

	public interface Fun1<T, N> extends Function<T, N> {

	}

	public interface Fun2<T, N, S> extends BiFunction<T, N, S> {

	}

	public interface VFun0 {
		public void apply();
	}

	public interface VFun1<T> {
		public void apply(T arg1);
	}

	public interface _Consumer<T> extends VFun1<T> {

	}

	public interface VFun2<T, N> {
		public void apply(T arg1, N arg2);
	}

	public static <T, N> Iterable<N> map(Iterable<T> iterable, Function<T, N> transformer) {
		return _Iterable.transform(iterable, transformer);
	}

	public static interface Accumulator<T> extends Function<T, Long> {

	}

	public static <T> long accumulate(Iterable<T> iterable, Accumulator<T> accumulator) {
		long total = 0;
		for (T element : iterable) {
			total = total + accumulator.apply(element);
		}
		return total;
	}

	@SuppressWarnings("unchecked")
	public static <T, N> Function<T, N> cast() {
		return (value) -> ((N) value);
	}
}