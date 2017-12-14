package com.anqit.util.lamqa.trials.exception;

import java.util.function.BiFunction;

import com.anqit.util.lamqa.function.Accepts;

/**
 * A consumer that handles an {@link Exception} thrown by a {@link BiFunction}.
 * A "tri-consumer" where the first argument is the {@link Exception} thrown,
 * and the second and third are designed to be the inputs to the {@link BiFunction}
 * that caused the exception
 *
 * @param <T>
 * 		The type of the first argument of the {@link BiFunction} that threw the exception
 * @param <U>
 * 		The type of the second argument of the {@link BiFunction} that threw the exception
 */
@FunctionalInterface
public interface BiFunctionExceptionHandler<T, U> extends Accepts<Exception> {
	/**
	 * The method to handle the exception
	 * 
	 * @param e
	 * @param t
	 * @param u
	 */
	void handle(Exception e, T t, U u);
	
	/**
	 * A no-op implementation
	 * 
	 * @return
	 * 		an excpetion handler that does nothing
	 */
	static <T, U> BiFunctionExceptionHandler<T, U> NOOP() {
		return (e, t, u) -> {};
	}
}
