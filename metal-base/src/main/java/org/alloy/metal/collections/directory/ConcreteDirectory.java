package org.alloy.metal.collections.directory;

import java.util.List;
import java.util.Optional;

import org.alloy.metal.collections.list.MutableList;
import org.alloy.metal.collections.list._Lists;

import com.google.common.collect.Lists;

public class ConcreteDirectory<T, N> implements Directory<T, N> {
	private MutableList<DirectoryIndex<T, N>> indexes = _Lists.list();

	@Override
	public boolean containsKey(T key) {
		if (!this.get(key).isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean containsValue(N value) {
		return indexes.flatMap((index) -> _Lists.wrap(index.getEntries()))
				.map((entry) -> entry.getValue())
				.contains(value);
	}

	@Override
	public List<N> get(T key) {
		return indexes.filter(_Directory.getDirectoryIndexKeyMatcher(key))
				.flatMap((index) -> _Lists.wrap(index.getEntries()))
				.map((entry) -> entry.getValue())
				.collectList()
				.asList();
	}

	@Override
	public List<N> get() {
		return indexes.flatMap((index) -> _Lists.wrap(index.getEntries()))
				.map((entry) -> entry.getValue())
				.collectList()
				.asList();
	}

	@Override
	public List<DirectoryEntry<T, N>> getEntries(T key) {
		return indexes.filter(_Directory.getDirectoryIndexKeyMatcher(key))
				.flatMap((index) -> _Lists.wrap(index.getEntries()))
				.collectList()
				.asList();
	}

	@Override
	public List<DirectoryEntry<T, N>> getEntries() {
		return indexes.flatMap((index) -> _Lists.wrap(index.getEntries()))
				.collectList()
				.asList();
	}

	@Override
	public List<DirectoryIndex<T, N>> getIndexes(T key) {
		return indexes.filter(_Directory.getDirectoryIndexKeyMatcher(key))
				.collectList()
				.asList();
	}

	@Override
	public List<DirectoryIndex<T, N>> getIndexes() {
		return _Lists.utilList(indexes);
	}

	@Override
	public DirectoryEntry<T, N> put(T key, N value) {
		return this.put(key, value, false);
	}

	@Override
	public DirectoryEntry<T, N> put(T key, N value, boolean createNew) {
		Optional<DirectoryIndex<T, N>> index = Optional.empty();
		if (!createNew) {
			index = indexes.filter(_Directory.getDirectoryIndexKeyMatcher(key))
					.single();
		}

		return index.orElseGet(() -> {
			DirectoryIndex<T, N> newIndex = _Directory.createIndex(key);
			indexes.add(newIndex);
			return newIndex;
		}).add(value);
	}

	@Override
	public boolean remove(T key) {
		boolean itemsRemoved = false;

		for (DirectoryIndex<T, N> index : this.getIndexes(key)) {
			indexes.remove(index);
			itemsRemoved = true;
		}
		return itemsRemoved;
	}

	@Override
	public void putAll(Directory<T, N> directory) {
		for (DirectoryEntry<T, N> entry : directory.getEntries()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		indexes.clear();
	}

	@Override
	public long size() {
		return indexes.map((index) -> index.size())
				.collect(0L, (result, value) -> result + value);
	}

	@Override
	public boolean isEmpty() {
		return indexes.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Directory: [");
		for (DirectoryIndex<T, N> index : this.getIndexes()) {
			sb.append(index);
			sb.append(", ");
		}
		sb.delete(sb.length() - ", ".length(), sb.length());
		sb.append("]");
		return sb.toString();
	}

	public static class ConcreteDirectoryIndex<T, N> implements DirectoryIndex<T, N> {
		private T key;
		private List<DirectoryEntry<T, N>> entries = Lists.newArrayList();

		@Override
		public T getKey() {
			return key;
		}

		@Override
		public List<DirectoryEntry<T, N>> getEntries() {
			return entries;
		}

		@Override
		public long size() {
			return entries.size();
		}

		@Override
		public DirectoryEntry<T, N> add(N value) {
			DirectoryEntry<T, N> entry = _Directory.createEntry(value, this);
			entries.add(entry);
			return entry;
		}

		@Override
		public void setKey(T key) {
			this.key = key;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Directory Index: {key = ");
			sb.append(key);
			sb.append(", entries = [");
			for (DirectoryEntry<T, N> entry : this.getEntries()) {
				sb.append(entry.getValue());
				sb.append(", ");
			}
			sb.delete(sb.length() - ", ".length(), sb.length());
			sb.append("}");
			return sb.toString();
		}
	}

	public static class ConcreteDirectoryEntry<T, N> implements DirectoryEntry<T, N> {
		private N value;
		private DirectoryIndex<T, N> index;

		@Override
		public N getValue() {
			return value;
		}

		@Override
		public T getKey() {
			return index.getKey();
		}

		@Override
		public void setValue(N value) {
			this.value = value;
		}

		@Override
		public void setIndex(DirectoryIndex<T, N> index) {
			this.index = index;
		}
	}
}
