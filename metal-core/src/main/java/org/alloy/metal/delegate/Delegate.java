package org.alloy.metal.delegate;

import java.util.function.Predicate;

import org.alloy.metal.order.Orderable;

public interface Delegate<N> extends Predicate<N>, Orderable {

}