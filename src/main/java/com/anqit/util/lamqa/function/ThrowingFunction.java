package com.anqit.util.lamqa.function;

import java.util.function.Function;

/**
 * An extension to {@link Function} that can throw a checked {@link Exception} 
 *
 * @param <T>
 * 		the type of the input to the function
 * @param <R>
 * 		the type of the result of the function
 */
public interface ThrowingFunction<T, R> extends Function<T, R>, Returns<R> {
	@Override
	default R apply(T t) {
		try {
			return applyThrows(t);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Applies this function to the given argument, possibly throwing an {@link Exception}
	 * @param t
	 * 		the function argument
	 * @return
	 * 		the function result
	 * @throws Exception
	 */
	R applyThrows(T t) throws Exception;
}
