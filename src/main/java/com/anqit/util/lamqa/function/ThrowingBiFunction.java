package com.anqit.util.lamqa.function;

import java.util.function.BiFunction;

/**
 * An extension to {@link BiFunction} that can throw a checked {@link Exception} 
 *
 * @param <T>
 * 		the type of the first argument to the function
 * @param <U>
 * 		the type of the second argument to the function
 * @param <R>
 * 		the type of the result of the function
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R> extends BiFunction<T, U, R>, Returns<R> {
	default R apply(T t, U u) {
		try {
			return applyThrows(t, u);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	/**
	 * Applies this function to the given arguments, possibly throwing an {@link Exception}
	 * @param t
	 * 		the first function argument
	 * @param u
	 * 		the second function argument
	 * @return
	 * 		the function result
	 * @throws Exception
	 */
	R applyThrows(T t, U u) throws Exception;
}
