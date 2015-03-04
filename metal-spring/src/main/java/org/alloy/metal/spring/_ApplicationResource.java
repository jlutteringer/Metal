package org.alloy.metal.spring;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.alloy.metal.collections.list.MutableList;
import org.alloy.metal.collections.list._Lists;
import org.alloy.metal.resource._Resource;
import org.alloy.metal.utility._Exception;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.google.common.collect.Lists;

public class _ApplicationResource {
	private static final Logger logger = LogManager.getLogger(_Resource.class);

	public static Resource getResource(String resourceLocation, ApplicationContext context) {
		return _Lists.wrap(getResources(resourceLocation, context)).single().orElse(null);
	}

	public static List<Resource> getResources(String resourceLocation, ApplicationContext context) {
		logger.trace("Resolving classpath resource at: [" + resourceLocation + "]");
		return _Exception.propagate(() -> Lists.newArrayList(context.getResources(resourceLocation)));
	}

	public static List<Resource> getConcreteResources(Resource resource) {
		logger.printf(Level.DEBUG, "Retrieving concrete file resources for resource [%s]", resource);
		MutableList<File> files = _Lists.list();
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

		List<Resource> concreteFileResources = files.map((file) -> file.getPath())
				.map(_Resource::getResourcePath)
				.map((path) -> getResource(path, Spring.getCurrentApplicationContext()))
				.collectList()
				.asList();

		logger.debug("Retrieved concrete file resources " + concreteFileResources);
		return concreteFileResources;
	}
}
