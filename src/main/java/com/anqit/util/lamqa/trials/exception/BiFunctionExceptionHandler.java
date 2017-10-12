package com.anqit.util.lamqa.trials.exception;

import com.anqit.util.lamqa.function.Accepts;

public interface BiFunctionExceptionHandler<T, U> extends Accepts<Exception> {
	void handle(Exception e, T t, U u);
	
	static <T, U> BiFunctionExceptionHandler<T, U> NOOP() {
		return (e, t, u) -> {};
	}
}
