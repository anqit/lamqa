package com.anqit.util.lamqa.trials.exception;

import java.util.function.BiConsumer;

import com.anqit.util.lamqa.function.Accepts;

public interface FunctionExceptionHandler<T> extends Accepts<Exception>, BiConsumer<Exception, T> {
	@Override
	default void accept(Exception e, T t) {
		handle(e, t);
	}
	
	void handle(Exception e, T t);
	
	static <T> FunctionExceptionHandler<T> NOOP() {
		return (e, t) -> {};
	}
}
