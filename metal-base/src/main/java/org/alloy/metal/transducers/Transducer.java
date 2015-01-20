package org.alloy.metal.transducers;

@FunctionalInterface
public interface Transducer<T, N> {
	public <R> Reducer<R, N> apply(Reducer<R, ? super T> reducer);

	public default <R> R transduce(Iterable<N> input, R initial, Reducer<R, ? super T> reducer) {
		return Transducers.transduce(this, input, initial, reducer);
	}

	public default <A> Transducer<A, N> combine(final Transducer<A, ? super T> right) {
		return new Transducer<A, N>() {
			@Override
			public <R> Reducer<R, N> apply(Reducer<R, ? super A> reducer) {
				return Transducer.this.apply(right.apply(reducer));
			}
		};
	}

	public interface StepFunction<T, N> {
		public T apply(T result, N input);
	}

	public interface Reducer<T, N> extends StepFunction<T, N> {
		public default T complete(T result) {
			return result;
		}

		public static HaltReductionExcpetion halt() {
			throw new HaltReductionExcpetion();
		}
	}

	public static class HaltReductionExcpetion extends RuntimeException {
		private static final long serialVersionUID = -1378832032859633919L;
	}
}