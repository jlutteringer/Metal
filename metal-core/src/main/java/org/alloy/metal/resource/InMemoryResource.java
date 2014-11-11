package org.alloy.metal.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.core.io.AbstractResource;
import org.springframework.util.Assert;

/**
 * An in memory implementation of Spring's {@link org.springframework.core.io.Resource} interface.
 * <p>Used to create a bean factory from an XML string, rather than a file.</p>
 *
 * @author Luke Taylor
 */
public class InMemoryResource extends AbstractResource {
	// ~ Instance fields ================================================================================================

	private final byte[] source;
	private final String description;

	// ~ Constructors ===================================================================================================

	public InMemoryResource(String source) {
		this(source.getBytes());
	}

	public InMemoryResource(byte[] source) {
		this(source, null);
	}

	public InMemoryResource(byte[] source, String description) {
		Assert.notNull(source);
		this.source = source;
		this.description = description;
	}

	// ~ Methods ========================================================================================================

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(source);
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object res) {
		if (!(res instanceof InMemoryResource)) {
			return false;
		}

		return Arrays.equals(source, ((InMemoryResource) res).source);
	}
}