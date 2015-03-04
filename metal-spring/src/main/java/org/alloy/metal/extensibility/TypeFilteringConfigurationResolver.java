package org.alloy.metal.extensibility;

import java.util.List;

import org.alloy.metal.collections.list._Lists;
import org.alloy.metal.reflection._Class;
import org.alloy.metal.reflection._Reflection;

public class TypeFilteringConfigurationResolver<T, N> extends AbstractConfigurationResolver<T, N> {
	@SuppressWarnings("unchecked")
	@Override
	protected List<N> resolveItems(List<T> configurations) {
		Class<?> filteringType = _Reflection.getTypeArguments(AbstractConfigurationResolver.class, this.getClass()).get(1);

		return _Lists.wrap(configurations)
				.filter(_Class.include(_Lists.utilList(filteringType)))
				.map(configuration -> (N) configuration)
				.collectList()
				.asList();
	}
}