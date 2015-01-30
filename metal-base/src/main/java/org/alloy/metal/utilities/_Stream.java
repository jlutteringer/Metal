package org.alloy.metal.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.alloy.metal.collections.flow.Flow;
import org.alloy.metal.string.CharacterEncoding;
import org.alloy.metal.string._String;
import org.alloy.metal.transducers.Reduction;
import org.alloy.metal.transducers.Transducer;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

public class _Stream {
	public static String toString(Reader reader) {
		return _Exception.propagate(() -> CharStreams.toString(reader));
	}

	public static String toString(InputStream from) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			br = new BufferedReader(new InputStreamReader(from, _String.CHARACTER_ENCODING));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			throw Throwables.propagate(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					throw Throwables.propagate(e);
				}
			}
		}

		return sb.toString();
	}

	public static <T> void withStream(InputStream stream, Consumer<InputStream> consumer) {
		withStream(Function.identity(), stream, consumer);
	}

	public static <T> void withStream(Function<T, InputStream> transformer, T item, Consumer<InputStream> consumer) {
		InputStream stream = null;
		try {
			stream = transformer.apply(item);
			consumer.accept(stream);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					throw Throwables.propagate(e);
				}
			}
		}
	}

	// TODO close this input stream
	public static void copy(InputStream stream, OutputStream out) {
		_Exception.propagate(() -> ByteStreams.copy(stream, out));
	}

	public static Flow<Byte> read(InputStream stream) {
		return null;
	}

	public static <T, N> Transducer<T, N> map(Function<N, T> function) {
		return new Transducer<T, N>() {
			@Override
			public <R> CompletingReducer<R, N> apply(CompletingReducer<R, ? super T> reducer) {
				return new ReducerOn<R, N>(reducer) {
					@Override
					public Reduction<R> reduce(R result, N input) {
						return reducer.reduce(result, function.apply(input));
					}
				};
			}
		};
	}

	public static Transducer<Character, Byte> convertBytes(CharacterEncoding encoding) {
		return new Transducer<Character, Byte>() {
			@Override
			public <R> CompletingReducer<R, Byte> apply(final CompletingReducer<R, ? super Character> reducer) {
				return new ReducerOn<R, Byte>(reducer) {
					private List<Byte> currentBytes = Lists.newArrayList();

					@Override
					public Reduction<R> reduce(R result, Byte input) {
						currentBytes.add(input);

						return Reduction.normal(result);
					}
				};
			}
		};
	}
}
