package org.alloy.metal.transducers;

import org.alloy.metal.transducers.Transducer.HaltReductionExcpetion;
import org.alloy.metal.transducers.Transducer.Reducer;

public class Transducers {
	private static <R, T> R reduce(Reducer<R, ? super T> reducingFunction, R result, Iterable<T> input) {
		R ret = result;
		for (T t : input) {
			try {
				ret = reducingFunction.apply(ret, t);
			} catch (HaltReductionExcpetion e) {
				break;
			}
		}
		return reducingFunction.complete(ret);
	}

	public static <R, A, B> R transduce(Transducer<A, B> transducer, Iterable<B> input, R initial, Reducer<R, ? super A> reducer) {
		Reducer<R, B> transducedFunction = transducer.apply(reducer);
		return reduce(transducedFunction, initial, input);
	}
}