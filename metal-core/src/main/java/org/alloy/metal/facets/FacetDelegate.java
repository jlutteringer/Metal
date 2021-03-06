package org.alloy.metal.facets;

import org.alloy.metal.delegate.Delegate;

public interface FacetDelegate<T extends Facet> extends Delegate<Class<? extends Facet>> {
	public <A extends Facet> A getFacet(Class<A> clazz, FacetedObject<?> object);
}