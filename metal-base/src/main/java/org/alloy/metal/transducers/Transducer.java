package org.alloy.metal.transducers;

import org.alloy.metal.collections.flow.Flow;

@FunctionalInterface
public interface Transducer<T, N> {
	public <R> CompletingReducer<R, N> apply(CompletingReducer<R, ? super T> reducer);

	public default <R> R transduce(Iterable<N> input, R initial, Reducer<R, ? super T> reducer) {
		return Transducers.transduce(this, input, initial, reducer);
	}

	public default <R> Flow<R> transduceDeferred(Flow<N> input, R initial, Reducer<R, ? super T> reducer) {
		return Transducers.transduceDeferred(this, input, initial, reducer);
	}

	public default <R> Iterable<R> transduceDeferred(Iterable<N> input, R initial, Reducer<R, ? super T> reducer) {
		return Transducers.transduceDeferred(this, input, initial, reducer);
	}

	public default <A> Transducer<A, N> combine(final Transducer<A, ? super T> right) {
		return new Transducer<A, N>() {
			@Override
			public <R> CompletingReducer<R, N> apply(CompletingReducer<R, ? super A> reducer) {
				return Transducer.this.apply(right.apply(reducer));
			}
		};
	}

	public interface Reducer<T, N> {
		public T reduce(T result, N input);
	}

	public interface CompletingReducer<T, N> {
		public Reduction<T> reduce(T result, N input);

		public Reduction<T> complete(T result);
	}

	public abstract class ReducerOn<T, N> implements CompletingReducer<T, N> {
		private CompletingReducer<T, ?> reducer;

		public ReducerOn(CompletingReducer<T, ?> reducer) {
			this.reducer = reducer;
		}

		@Override
		public Reduction<T> complete(T result) {
			return reducer.complete(result);
		}
	}
}