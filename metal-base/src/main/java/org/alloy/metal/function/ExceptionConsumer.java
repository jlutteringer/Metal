package org.alloy.metal.function;

@FunctionalInterface
public interface ExceptionConsumer<T, N extends Exception> {
	public void accept(T t) throws N;
}