package com.anqit.util.lamqa.trials.exception;

import java.util.function.Consumer;

import com.anqit.util.lamqa.function.Accepts;

/**
 * An {@link Exception} consumer to handle thrown {@link Exception}s
 */
@FunctionalInterface
public interface ExceptionHandler extends Consumer<Exception>, Accepts<Exception> {
	@Override
	default void accept(Exception e) {
		handle(e);
	}
	
	/**
	 * The exception handling method
	 * 
	 * @param e
	 * 		the {@link Exception} to handle
	 */
	void handle(Exception e);
	
	/**
	 * A no-op excpetion handler
	 */
	static ExceptionHandler NOOP = e -> {};
}
