package org.alloy.metal.extensibility;

import java.util.List;

import org.alloy.metal.collections.iterable._Iterable;
import org.alloy.metal.collections.lists._Lists;
import org.alloy.metal.function.OldFunction;
import org.alloy.metal.reflection._Class;
import org.alloy.metal.reflection._Reflection;

public class TypeFilteringConfigurationResolver<T, N> extends AbstractConfigurationResolver<T, N> {
	@Override
	protected List<N> resolveItems(List<T> configurations) {
		Class<?> filteringType = _Reflection.getTypeArguments(AbstractConfigurationResolver.class, this.getClass()).get(1);
		Iterable<T> filteredConfigurations = _Iterable.filter(configurations, _Class.include(_Lists.list(filteringType)));
		return _Lists.list(_Iterable.transform(filteredConfigurations, OldFunction.cast()));
	}
}