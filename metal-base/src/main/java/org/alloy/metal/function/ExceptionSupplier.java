package org.alloy.metal.function;

@FunctionalInterface
public interface ExceptionSupplier<T, N extends Exception> {
	public T next() throws N;
}