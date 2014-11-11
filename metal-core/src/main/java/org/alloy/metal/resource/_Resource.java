package org.alloy.metal.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import org.alloy.metal.collections.iterable._Iterable;
import org.alloy.metal.function.ExceptionConsumer;
import org.alloy.metal.function.ExceptionFunction;
import org.alloy.metal.reflection._ClassPath;
import org.alloy.metal.utilities._Closeable;
import org.alloy.metal.utilities._Exception;
import org.alloy.metal.utilities._Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;

public class _Resource {
	private static final Logger logger = LogManager.getLogger(_Resource.class);

	public static Function<Resource, InputStream> transformer() {
		return (resource) -> {
			try {
				return resource.getInputStream();
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		};
	}

	public static boolean exists(Resource resource) {
		if (resource != null && resource.exists()) {
			return true;
		}
		return false;
	}

	public static Resource findExistingRelative(Resource location, String relativePath) {
		try {
			logger.debug("Trying relative path [" + relativePath + "] against base location: " + location);

			Resource resource = location.createRelative(relativePath);
			if (exists(resource)) {
				logger.debug("Found matching resource: " + resource);
				return resource;
			}
			else {
				logger.trace("Relative resource doesn't exist or isn't readable: " + resource);
			}
		} catch (IOException ex) {
			logger.debug("Failed to create relative resource - trying next resource location", ex);
		}

		return null;
	}

	public static byte[] getBytes(Resource resource) {
		try {
			return ByteStreams.toByteArray(resource.getInputStream());
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	public static String getPath(Resource resource) {
		if (resource instanceof UrlResource || resource instanceof ClassPathResource) {
			try {
				String path = resource.getURL().getPath();
				logger.debug(String.format("Found path for resource: [%s] path :[%s]", resource, path));
				return path;
			} catch (IOException e) {
				throw Throwables.propagate(e);
			}
		}
		else {
			return resource.getDescription();
		}
	}

	public static InputStream getInputStream(Resource resource) {
		return _Exception.propagate(() -> {
			return resource.getInputStream();
		});
	}

	public static void getInputStream(Resource resource, ExceptionConsumer<InputStream> consumer) {
		_Closeable.with(getInputStream(resource), consumer);
	}

	public static <T> T getInputStreamWithResult(Resource resource, ExceptionFunction<InputStream, T> function) {
		return _Closeable.withResult(getInputStream(resource), function);
	}

	public static boolean isValidResource(Resource resource) {
		if (resource.exists() && resource.isReadable()) {
			return true;
		}
		return false;
	}

	public static Iterable<String> getResourcePaths(Iterable<String> paths) {
		List<String> classpathEntries = _ClassPath.getClasspathEntries();
		return _Iterable.transform(paths, (path) -> {
			for (String classpathEntry : classpathEntries) {
				if (path.startsWith(classpathEntry)) {
					return path.replace(classpathEntry, "");
				}
			}

			return path;
		});
	}

	public static String stringify(Resource resource) {
		return getInputStreamWithResult(resource, _Stream::toString);
	}

	public static Resource toResource(byte[] bytes) {
		return new InMemoryResource(bytes);
	}

	public static Resource toResource(ByteArrayOutputStream os) {
		return toResource(os.toByteArray());
	}

	public static Resource toResource(String resourceData) {
		return new GeneratedResource(resourceData.getBytes(), "generatedResource");
	}
}