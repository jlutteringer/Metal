package org.alloy.metal.spring.facet;

import java.util.function.Predicate;

import org.alloy.metal.facets.Facet;
import org.alloy.metal.facets.FacetDelegate;
import org.alloy.metal.facets.FacetedObject;
import org.alloy.metal.reflection._Reflection;
import org.alloy.metal.spring.delegator.AbstractDelegate;

public abstract class AbstractFacetDelegate<T extends N, N extends Facet> extends AbstractDelegate<Class<? extends Facet>> implements FacetDelegate<T> {
	@Override
	protected Predicate<Class<? extends Facet>> getInternalMatcher() {
		return (input) -> input.isAssignableFrom(this.getFacetType());
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getFacetType() {
		return (Class<T>) _Reflection.getTypeArguments(AbstractFacetDelegate.class, this.getClass()).get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <A extends Facet> A getFacet(Class<A> clazz, FacetedObject<?> object) {
		return (A) this.getFacetInternal((FacetedObject<N>) object);
	}

	protected abstract T getFacetInternal(FacetedObject<N> object);
}