package org.alloy.metal.spring;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.alloy.metal.collections.iterable._Iterable;
import org.alloy.metal.collections.lists._List;
import org.alloy.metal.resource._Resource;
import org.alloy.metal.utilities._Exception;
import org.alloy.metal.utilities._File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.google.common.collect.Lists;

public class _ApplicationResource {
	private static final Logger logger = LogManager.getLogger(_Resource.class);

	public static Resource getResource(String resourceLocation, ApplicationContext context) {
		return _Iterable.getSingleResult(getResources(resourceLocation, context));
	}

	public static List<Resource> getResources(String resourceLocation, ApplicationContext context) {
		logger.trace("Resolving classpath resource at: [" + resourceLocation + "]");
		return _Exception.propagate(() -> Lists.newArrayList(context.getResources(resourceLocation)));
	}

	public static List<Resource> getConcreteResources(Resource resource) {
		logger.printf(Level.DEBUG, "Retrieving concrete file resources for resource [%s]", resource);
		List<File> files = Lists.newArrayList();
		LinkedList<File> filesToProcess = Lists.newLinkedList();
		filesToProcess.add(new File(_Resource.getPath(resource)));

		while (!filesToProcess.isEmpty()) {
			File file = filesToProcess.pop();
			if (!file.isDirectory()) {
				files.add(file);
			}
			else {
				filesToProcess.addAll(Arrays.asList(file.listFiles()));
			}
		}

		List<Resource> concreteFileResources = _List.transform(
				_Resource.getResourcePaths(_File.getPaths(files)), (path) -> getResource(path, Spring.getCurrentApplicationContext()));

		logger.debug("Retrieved concrete file resources " + concreteFileResources);
		return concreteFileResources;
	}
}
