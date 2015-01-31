package org.alloy.metal.function;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.alloy.metal.collections.flow.FlowCompletion;

public class Main {
	public static void main(String[] args) {
		new Consuming(new Sending()).run();
	}

	public static class Consuming implements Runnable {
		private Sending input;

		public Consuming(Sending input) {
			this.input = input;
		}

		@Override
		public void run() {
			Flow2<Integer> flow = input.getFlow();
			flow.forEach((number) -> {
				System.out.println(number);
			});
		}
	}

	public static class Sending implements Runnable {
		private Supplier<Integer> input = () -> 1;

		@Override
		public void run() {

		}

		public Flow2<Integer> getFlow() {
			return new Flow2<Integer>() {
				@Override
				public FlowCompletion flow(FlowDescription description, Consumer<Integer> consumer) {
					consumer.accept(input.get());
					return (callback) -> callback.apply();
				}
			};
		}
	}

	public static class FlowDescription {
		public static FlowDescription all() {
			return null;
		}

		public static FlowDescription one() {
			return null;
		}

		public static FlowDescription limit(long limit) {
			return null;
		}
	}

	@FunctionalInterface
	public interface Flow2<T> {
		public FlowCompletion flow(FlowDescription description, Consumer<T> consumer);

		public default FlowCompletion forNext(Consumer<T> consumer) {
			return flow(FlowDescription.one(), consumer);
		}

		public default FlowCompletion forEach(Consumer<T> consumer) {
			return flow(FlowDescription.all(), consumer);
		}
	}
}