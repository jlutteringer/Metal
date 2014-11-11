package org.alloy.metal.delegate;

public interface Delegator<T extends Delegate<N>, N> {
	public T getDelegate(N delegatee);
}
