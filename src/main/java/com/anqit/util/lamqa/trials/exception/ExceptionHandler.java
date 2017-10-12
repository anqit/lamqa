package com.anqit.util.lamqa.trials.exception;

import java.util.function.Consumer;

import com.anqit.util.lamqa.function.Accepts;

public interface ExceptionHandler extends Consumer<Exception>, Accepts<Exception> {
	@Override
	default void accept(Exception e) {
		handle(e);
	}
	
	void handle(Exception e);
	
	static ExceptionHandler NOOP = e -> {};
}
