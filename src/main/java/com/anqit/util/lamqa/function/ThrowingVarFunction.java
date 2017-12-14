package com.anqit.util.lamqa.function;

/**
 * A function that can take an arbitrary number of arguments
 *
 * @param <R>
 * 		the return type of the function
 */
public interface ThrowingVarFunction<R> {
	/**
	 * Applies this function to the given aruments
	 * 
	 * @param objects
	 * 		the inputs to the function
	 * @return
	 * 		the result of the function applied to the given arguments
	 * 
	 * @throws Exception
	 */
	<T extends Object> R apply(@SuppressWarnings("unchecked") T... objects) throws Exception;
}
