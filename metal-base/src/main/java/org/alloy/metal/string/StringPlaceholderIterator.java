package org.alloy.metal.string;

import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;

import org.alloy.metal.collections.iterable.SingleEntryIterator;
import org.alloy.metal.string.StringPlaceholderIterator.StringPlaceholder;
import org.alloy.metal.utilities._Precondition;

public class StringPlaceholderIterator extends SingleEntryIterator<StringPlaceholder> {
	private final String placeholderPrefix;
	private final String placeholderSuffix;
	private final String source;

	private int currentLocation = 0;

	public StringPlaceholderIterator(String placeholderPrefix, String placeholderSuffix, String source) {
		_Precondition.notNull(placeholderPrefix, placeholderSuffix);

		this.placeholderPrefix = placeholderPrefix;
		this.placeholderSuffix = placeholderSuffix;
		this.source = _String.getDefault(source);
	}

	@Override
	protected StringPlaceholder generateNext() throws NoSuchElementException {
		int sourceLength = source.length();

		int placeholderPrefixIndex = source.indexOf(placeholderPrefix, currentLocation);
		if (placeholderPrefixIndex == -1) {
			throw new NoSuchElementException();
		}

		int placeholderSuffixIndex = source.indexOf(placeholderSuffix, currentLocation);
		if (placeholderPrefixIndex == -1) {
			throw new NoSuchElementException();
		}

		throw new NoSuchElementException();
	}

	public static class StringPlaceholder implements Serializable {
		private static final long serialVersionUID = -8252126400328088545L;

		private final String name;
		private final int sourceIndex;

		public StringPlaceholder(String name, int sourceIndex) {
			super();
			this.name = name;
			this.sourceIndex = sourceIndex;
		}

		public String getName() {
			return name;
		}

		public int getSourceIndex() {
			return sourceIndex;
		}

		public static Comparator<StringPlaceholder> comparator() {
			// TODO
			return null;
		}
	}
}