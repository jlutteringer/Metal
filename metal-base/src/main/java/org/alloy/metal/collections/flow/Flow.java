package org.alloy.metal.collections.flow;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface Flow<T> {
	public FlowCompletion forCompletion(Function<T, Boolean> consumer);

	public default FlowCompletion forEach(Consumer<T> consumer) {
		return this.forCompletion((element) -> {
			consumer.accept(element);
			return true;
		});
	}
}