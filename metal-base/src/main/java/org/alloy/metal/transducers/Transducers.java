package org.alloy.metal.transducers;

import java.util.function.Function;

import org.alloy.metal.collections.flow.AsyncFlowCompletion;
import org.alloy.metal.collections.flow.Flow;
import org.alloy.metal.collections.flow.FlowCompletion;
import org.alloy.metal.collections.flow._Flows;
import org.alloy.metal.collections.iterable.GeneratingIterable;
import org.alloy.metal.transducers.Transducer.CompletingReducer;
import org.alloy.metal.transducers.Transducer.Reducer;

public class Transducers {
	public static class ReductionFlow<R, T> implements Flow<R> {
		private CompletingReducer<R, ? super T> reducingFunction;
		private Flow<T> input;
		private R result;

		public ReductionFlow(CompletingReducer<R, ? super T> reducingFunction, Flow<T> input, R initial) {
			super();
			this.result = initial;
			this.reducingFunction = reducingFunction;
			this.input = input;
		}

		@Override
		public FlowCompletion forCompletion(Function<R, Boolean> consumer) {
			AsyncFlowCompletion completion = new AsyncFlowCompletion();

			input.forCompletion((value) -> {
				boolean cont = true;

				Reduction<R> reduction = reducingFunction.reduce(result, value);
				result = reduction.getValue();

				if (!reduction.isPartialReduction()) {
					cont = consumer.apply(result);
				}
				if (reduction.isReductionHalted()) {
					cont = false;
				}

				return cont;
			}).whenComplete(() -> {
				boolean cont = true;

				while (cont) {
					Reduction<R> reduction = Reduction.incomplete(result);
					while (reduction.isPartialReduction() && !reduction.isReductionHalted()) {
						reduction = reducingFunction.complete(result);
					}

					if (!reduction.isPartialReduction()) {
						result = reduction.getValue();
						cont = consumer.apply(result);
					}

					if (reduction.isReductionHalted()) {
						cont = false;
					}
				}

				completion.trigger();
			});

			return completion;
		}
	}

	private static <R, T> R reduce(CompletingReducer<R, ? super T> reducingFunction, R result, Iterable<T> input) {
		R ret = result;
		for (T t : input) {
			Reduction<R> reduction = reducingFunction.reduce(ret, t);
			ret = reduction.getValue();

			if (reduction.isReductionHalted()) {
				break;
			}
		}

		boolean completionHalted = false;
		while (!completionHalted) {
			Reduction<R> reduction = reducingFunction.complete(ret);
			ret = reduction.getValue();

			if (reduction.isReductionHalted()) {
				completionHalted = true;
			}
		}

		return ret;
	}

	public static <R, A, B> R transduce(Transducer<A, B> transducer, Iterable<B> input, R initial, Reducer<R, ? super A> reducer) {
		CompletingReducer<R, B> transducedFunction = transducer.apply(baseReducer(reducer));
		return reduce(transducedFunction, initial, input);
	}

	public static <R, A, B> Flow<R> transduceDeferred(Transducer<A, B> transducer, Flow<B> input, R initial, Reducer<R, ? super A> reducer) {
		CompletingReducer<R, B> transducedFunction = transducer.apply(baseReducer(reducer));
		return new ReductionFlow<>(transducedFunction, input, initial);
	}

	public static <R, A, B> Iterable<R> transduceDeferred(Transducer<A, B> transducer, Iterable<B> input, R initial, Reducer<R, ? super A> reducer) {
		return new GeneratingIterable<R>(() -> {
			Flow<R> flow = transduceDeferred(transducer, _Flows.flow(input), initial, reducer);
			return _Flows.iterator(flow);
		});
	}

	private static <R, A> CompletingReducer<R, A> baseReducer(Reducer<R, A> reducer) {
		return new CompletingReducer<R, A>() {
			@Override
			public Reduction<R> reduce(R result, A input) {
				return Reduction.normal(reducer.reduce(result, input));
			}

			@Override
			public Reduction<R> complete(R result) {
				return Reduction.haltIncomplete(result);
			}
		};
	}
}