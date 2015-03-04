package org.alloy.metal.spring.delegator;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.alloy.metal.collections.list._Lists;
import org.alloy.metal.delegate.Delegate;
import org.alloy.metal.delegate.Delegator;
import org.alloy.metal.order.Orderable;
import org.alloy.metal.utility._Predicate;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDelegator<T extends Delegate<N>, N> implements Delegator<T, N> {
	@Autowired
	private List<T> delegates;

	@PostConstruct
	private void init() {
		Collections.sort(delegates, Orderable.comparator());
	}

	@Override
	public T getDelegate(N delegatee) {
		return _Lists.wrap(delegates)
				.filter(_Predicate.reverse(delegatee))
				.first()
				.orElseThrow(() -> new RuntimeException("No matching delegate found for delegatee " + delegatee));
	}

	public List<T> getDelegates() {
		return delegates;
	}
}