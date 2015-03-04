package org.alloy.metal.reflection;

import java.io.File;

import org.alloy.metal.collections.list.AList;
import org.alloy.metal.collections.list.MutableList;
import org.alloy.metal.collections.list._Lists;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import com.google.common.base.Throwables;

public class _ClassPath {
	public static final AList<String> CLASSPATH_ENTRIES;

	static {
		String[] classpathEntries = System.getProperty("java.class.path").split(File.pathSeparator);
		CLASSPATH_ENTRIES = _Lists.immutableList(classpathEntries);
	}

	public static AList<String> getClasspathEntries() {
		return CLASSPATH_ENTRIES;
	}

	@SuppressWarnings("unchecked")
	public static <T> AList<Class<T>> scanForType(String basePackage, Class<T> type) {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

		MutableList<Class<T>> candidates = _Lists.list();

		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
				resolveBasePackage(basePackage) + "/" + "**/*.class";

		try {
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
			for (Resource resource : resources) {
				if (resource.isReadable()) {
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);

					Class<?> canidate = Class.forName(metadataReader.getClassMetadata().getClassName());
					if (type.isAssignableFrom(canidate)) {
						candidates.add((Class<T>) canidate);
					}
				}
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}

		return candidates;
	}

	private static String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}
}
