package org.alloy.metal.collections.tree;

import java.util.Collection;
import java.util.List;

import org.alloy.metal.collections.MutableCollection;
import org.alloy.metal.collections.set._Sets;
import org.alloy.metal.iteration._Iteration;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class _Tree {
	public static <T> HashTree<T> newHashTree(T head) {
		return new HashTree<T>(head);
	}

	public static <T> Collection<T> flatten(Tree<T> tree) {
		List<T> flattened = Lists.newArrayList();
		for (T element : _Tree.iterateBreadthFirst(tree)) {
			flattened.add(element);
		}
		return flattened;
	}

	public static <T> Iterable<T> iterateBreadthFirst(final Tree<T> topLevelTree) {
		return _Iteration.<T> iterable(() -> {
			MutableCollection<Tree<T>> elementsToProcess = _Sets.set();
			MutableCollection<Tree<T>> visitedElements = _Sets.set();
			elementsToProcess.add(topLevelTree);

			return (consumer) -> {
				Tree<T> tree = null;
				for (Tree<T> elementToProcess : elementsToProcess) {
					if (visitedElements.containsAll(elementToProcess.getParents())) {
						tree = elementToProcess;
						break;
					}
				}

				if (tree == null) {
					return false;
				}

				elementsToProcess.remove(tree);
				for (Tree<T> subTree : tree.getChildren()) {
					elementsToProcess.add(subTree);
				}

				visitedElements.add(tree);
				consumer.accept(tree.getHead());
				return true;
			};
		});
	}

	public static <T> String stringify(Tree<T> tree) {
		StringBuilder builder = new StringBuilder();
		stringifyInternal(tree, builder, 0);
		return builder.toString();
	}

	private static <T> void stringifyInternal(Tree<T> tree, StringBuilder builder, int depth) {
		builder.append(StringUtils.repeat("\t", depth));
		builder.append(tree.getHead().toString());
		builder.append("\n");

		for (Tree<T> child : tree.getChildren()) {
			stringifyInternal(child, builder, depth + 1);
		}
	}
}
