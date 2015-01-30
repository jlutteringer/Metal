package org.alloy.metal.collections.flow;

import org.alloy.metal.function.Operation;

public interface FlowCompletion {
	public void whenComplete(Operation callback);
}