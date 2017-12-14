package com.anqit.util.lamqa.trials.exception;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.anqit.util.lamqa.function.Accepts;

/**
 * A consumer that handles an {@link Exception} thrown by a {@link Function}.
 * An extension to {@link BiConsumer} where the first argument is the {@link Exception} thrown,
 * and the second is designed to be the input to the {@link Function} that caused the exception
 *
 * @param <T>
 * 		The input type of the {@link Function} that threw the exception
 */
@FunctionalInterface
public interface FunctionExceptionHandler<T> extends Accepts<Exception>, BiConsumer<Exception, T> {
	@Override
	default void accept(Exception e, T t) {
		handle(e, t);
	}
	
	/**
	 * the {@link Exception} handling method
	 * 
	 * @param e
	 * 		the exception to handle
	 * @param t
	 * 		the input that caused the {@link Function} to throw the exception
	 */
	void handle(Exception e, T t);
	
	/**
	 * A no-op implementation
	 * 
	 * @return
	 * 		a exception handler that does nothing
	 */
	static <T> FunctionExceptionHandler<T> NOOP() {
		return (e, t) -> {};
	}
}
