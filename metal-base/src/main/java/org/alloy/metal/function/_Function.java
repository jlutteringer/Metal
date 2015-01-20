package org.alloy.metal.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.alloy.metal.transducers.Transducer;
import org.alloy.metal.transducers.Transducer.Reducer;

import com.google.common.collect.Lists;

public class _Function {
	public static <T, N> Transducer<T, N> map(Function<N, T> function) {
		return new Transducer<T, N>() {
			@Override
			public <R> Reducer<R, N> apply(Reducer<R, ? super T> reducer) {
				return new Reducer<R, N>() {
					@Override
					public R apply(R result, N input) {
						return reducer.apply(result, function.apply(input));
					}
				};
			}
		};
	}

	public static <A, P> Transducer<Iterable<A>, A> partitionBy(final Function<A, P> f) {
		return new Transducer<Iterable<A>, A>() {
			@Override
			public <R> Reducer<R, A> apply(final Reducer<R, ? super Iterable<A>> rf) {
				return new Reducer<R, A>() {
					List<A> part = new ArrayList<A>();
					Object mark = new Object();
					Object prior = mark;

					@Override
					public R complete(R result) {
						R ret = result;
						if (!part.isEmpty()) {
							List<A> copy = new ArrayList<A>(part);
							part.clear();
							ret = rf.apply(result, copy);
						}
						return rf.complete(ret);
					}

					@Override
					public R apply(R result, A input) {
						P val = f.apply(input);
						if ((prior == mark) || (prior.equals(val))) {
							prior = val;
							part.add(input);
							return result;
						} else {
							List<A> copy = new ArrayList<A>(part);
							prior = val;
							part.clear();
							R ret = rf.apply(result, copy);
							if (!false) {
								part.add(input);
							}
							return ret;
						}
					}
				};
			}
		};
	}

	public static void test() {
		Transducer<String, Integer> transducer = map((a) -> a.toString());
		Transducer<Long, String> transducer2 = map((a) -> Long.parseLong(a));

		Reducer<Object, Integer> reducer = transducer.apply(transducer2.apply(null));
		Transducer<Long, Integer> combined = transducer.combine(transducer2);

		Long l = combined.transduce(Lists.newArrayList(1, 2, 3), 0L, (result, input) -> result + input);

		List<String> strings = transducer.transduce(Lists.newArrayList(1, 2, 3), Lists.newArrayList(), (result, input) -> {
			result.add(input);
			return result;
		});
	}
}