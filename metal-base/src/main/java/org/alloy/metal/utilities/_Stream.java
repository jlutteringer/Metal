package org.alloy.metal.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.function.Consumer;
import java.util.function.Function;

import org.alloy.metal.string._String;

import com.google.common.base.Throwables;
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
			br = new BufferedReader(new InputStreamReader(from, _String.CHARACTER_ENCODING.toString()));
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

//	public static Flow<ByteArray> read(InputStream stream) {
//		return null;
//	}

//	public static Flow<String> toString(Flow<ByteArray> flow, long size) {
//		return toCharacters(_String.CHARACTER_ENCODING)
//				.combine(toString(size))
//				.combine(toLine())
//				.transduceDeferred(flow);
//	}

//	public static <T, N> Transducer<T, N> map(Function<N, T> function) {
//		return new Transducer<T, N>() {
//			@Override
//			public <R> CompletingReducer<R, N> apply(CompletingReducer<R, ? super T> reducer) {
//				return new ReducerOn<R, N>(reducer) {
//					@Override
//					public Reduction<R> reduce(R result, N input) {
//						return reducer.reduce(result, function.apply(input));
//					}
//				};
//			}
//		};
//	}
//
//	public static Transducer<CharSequence, ByteArray> toCharacters(CharacterEncoding encoding) {
//		return new Transducer<CharSequence, ByteArray>() {
//			@Override
//			public <R> CompletingReducer<R, ByteArray> apply(final CompletingReducer<R, ? super CharSequence> reducer) {
//				return new ReducerOn<R, ByteArray>(reducer) {
//					@Override
//					public Reduction<R> reduce(R result, ByteArray input) {
//						return Reduction.normal(result);
//					}
//				};
//			}
//		};
//	}
//
//	public static Transducer<String, CharSequence> toString(long size) {
//		return new Transducer<String, CharSequence>() {
//			@Override
//			public <R> CompletingReducer<R, CharSequence> apply(final CompletingReducer<R, ? super String> reducer) {
//				return new ReducerOn<R, CharSequence>(reducer) {
//					@Override
//					public Reduction<R> reduce(R result, CharSequence input) {
//						return Reduction.normal(result);
//					}
//				};
//			}
//		};
//	}
//
//	public static Transducer<String, String> toLine() {
//		return new Transducer<String, String>() {
//			@Override
//			public <R> CompletingReducer<R, String> apply(final CompletingReducer<R, ? super String> reducer) {
//				return new ReducerOn<R, String>(reducer) {
//					@Override
//					public Reduction<R> reduce(R result, String input) {
//						return Reduction.normal(result);
//					}
//				};
//			}
//		};
//	}
}