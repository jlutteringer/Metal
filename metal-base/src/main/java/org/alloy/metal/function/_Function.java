package org.alloy.metal.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.alloy.metal.collections.flow.Flow;
import org.alloy.metal.collections.flow._Flows;
import org.alloy.metal.transducers.Reduction;
import org.alloy.metal.transducers.Transducer;

import com.google.common.collect.Lists;

public class _Function {
	/**
	* Creates a transducer that transforms a reducing function by applying a
	* predicate to each input and processing only those inputs for which the
	* predicate is true.
	* @param p a predicate function
	* @param <A> input type of input and output reducing functions
	* @return a new transducer
	*/
	public static <A> Transducer<A, A> filter(final Predicate<A> p) {
		return new Transducer<A, A>() {
			@Override
			public <R> CompletingReducer<R, A> apply(CompletingReducer<R, ? super A> rf) {
				return new ReducerOn<R, A>(rf) {
					@Override
					public Reduction<R> reduce(R result, A input) {
						if (p.test(input)) {
							return rf.reduce(result, input);
						}

						return Reduction.incomplete(result);
					}
				};
			};
		};
	}

	/**
	* Creates a transducer that transforms a reducing function such that
	* it only processes n inputs, then the reducing process stops.
	* @param n the number of inputs to process
	* @param <A> input type of input and output reducing functions
	* @return a new transducer
	*/
	public static <A> Transducer<A, A> take(final long n) {
		return new Transducer<A, A>() {
			@Override
			public <R> CompletingReducer<R, A> apply(CompletingReducer<R, ? super A> rf) {
				return new ReducerOn<R, A>(rf) {
					long taken = 0;

					@Override
					public Reduction<R> reduce(R result, A input) {
						if (taken < n - 1) {
							taken++;
							return rf.reduce(result, input);
						}
						else if (taken < n) {
							return Reduction.halt(rf.reduce(result, input));
						}
						else {
							return Reduction.haltIncomplete(result);
						}
					}
				};
			}
		};
	}

	public static <A> Transducer<A, A> sort(final Comparator<A> comparator) {
		return new Transducer<A, A>() {
			@Override
			public <R> CompletingReducer<R, A> apply(CompletingReducer<R, ? super A> rf) {
				return new CompletingReducer<R, A>() {
					List<A> inputs = Lists.newArrayList();
					List<A> sortedInputs = null;
					int currentIndex = 0;

					@Override
					public Reduction<R> complete(R result) {
						if (sortedInputs == null) {
							Collections.sort(inputs, comparator);
							sortedInputs = inputs;
						}

						if (currentIndex < sortedInputs.size()) {
							currentIndex++;
							return rf.reduce(result, sortedInputs.get(currentIndex - 1));
						}
						else {
							return rf.complete(result);
						}
					}

					@Override
					public Reduction<R> reduce(R result, A input) {
						inputs.add(input);
						return Reduction.incomplete(result);
					}
				};
			};
		};
	}

	public static <T, N> Transducer<T, N> map(Function<N, T> function) {
		return new Transducer<T, N>() {
			@Override
			public <R> CompletingReducer<R, N> apply(CompletingReducer<R, ? super T> reducer) {
				return new ReducerOn<R, N>(reducer) {
					@Override
					public Reduction<R> reduce(R result, N input) {
						return reducer.reduce(result, function.apply(input));
					}
				};
			}
		};
	}

	public static <A, P> Transducer<Iterable<A>, A> partitionBy(final Function<A, P> f) {
		return new Transducer<Iterable<A>, A>() {
			@Override
			public <R> CompletingReducer<R, A> apply(final CompletingReducer<R, ? super Iterable<A>> rf) {
				return new CompletingReducer<R, A>() {
					List<A> part = new ArrayList<A>();
					Object mark = new Object();
					Object prior = mark;

					@Override
					public Reduction<R> complete(R result) {
						R ret = result;
						if (!part.isEmpty()) {
							List<A> copy = new ArrayList<A>(part);
							part.clear();
							return rf.reduce(result, copy);
						}

						return rf.complete(ret);
					}

					@Override
					public Reduction<R> reduce(R result, A input) {
						P val = f.apply(input);
						if ((prior == mark) || (prior.equals(val))) {
							prior = val;
							part.add(input);
							return Reduction.incomplete(result);
						} else {
							List<A> copy = new ArrayList<A>(part);
							prior = val;
							part.clear();
							part.add(input);
							return rf.reduce(result, copy);
						}
					}
				};
			}
		};
	}

	/**
	* Creates a transducer that transforms a reducing function that processes
	* iterables of input into a reducing function that processes individual inputs
	* by gathering series of inputs into partitions of a given size, only forwarding
	* them to the next reducing function when enough inputs have been accrued. Processes
	* any remaining buffered inputs when the reducing process completes.
	* @param n the size of each partition
	* @param <A> the input type of the input and output reducing functions
	* @return a new transducer
	*/
	public static <A> Transducer<Iterable<A>, A> partitionAll(int n) {
		return new Transducer<Iterable<A>, A>() {
			@Override
			public <R> CompletingReducer<R, A> apply(final CompletingReducer<R, ? super Iterable<A>> rf) {
				return new CompletingReducer<R, A>() {
					List<A> part = new ArrayList<A>(n);

					@Override
					public Reduction<R> complete(R result) {
						R ret = result;
						if (!part.isEmpty()) {
							List<A> copy = new ArrayList<A>(part);
							part.clear();
							return rf.reduce(result, copy);
						}

						return rf.complete(ret);
					}

					@Override
					public Reduction<R> reduce(R result, A input) {
						part.add(input);
						if (n == part.size()) {
							List<A> copy = new ArrayList<A>(part);
							part.clear();
							return rf.reduce(result, copy);
						}

						return Reduction.incomplete(result);
					}
				};
			}
		};
	}

	public static void main(String[] args) {

		Transducer<String, Integer> toString = map((a) -> {
			System.out.println("Mapping " + a);
			return a.toString();
		});
		Transducer<Long, String> parseLong = map((a) -> Long.parseLong(a));
		Transducer<Long, Long> sort = sort((a, b) -> a.compareTo(b));
		Transducer<Iterable<Long>, Long> partition = partitionAll(2);
		Transducer<Long, Long> take = take(3);

		Transducer<Iterable<Long>, Integer> combined =
				toString.combine(parseLong).combine(sort).combine(partition);

		Flow<Iterable<Long>> test2 = combined.transduceDeferred(_Flows.flow(Lists.newArrayList(3, 2, 1, 6, 23, 3, 3)), Lists.newArrayList(), (result, input) -> input);
		test2.forEach((iterable) -> {
			iterable.forEach(System.out::println);
			System.out.println("-");
		});

		System.out.println("=========================");

		List<Iterable<Long>> test3 = combined.transduce(Lists.newArrayList(3, 2, 1, 6, 23, 3, 3), Lists.newArrayList(), (result, input) -> {
			result.add(input);
			return result;
		});
		test3.forEach((iterable) -> {
			iterable.forEach(System.out::println);
			System.out.println("-");
		});

		System.out.println("=========================");

		Transducer<Iterable<Long>, Integer> combined2 =
				toString.combine(parseLong).combine(take).combine(partition);

		Flow<Iterable<Long>> test4 = combined2.transduceDeferred(_Flows.flow(Lists.newArrayList(3, 2, 1, 6, 23, 3, 3)), Lists.newArrayList(), (result, input) -> input);
		test4.forEach((iterable) -> {
			iterable.forEach(System.out::println);
			System.out.println("-");
		});

	}
}