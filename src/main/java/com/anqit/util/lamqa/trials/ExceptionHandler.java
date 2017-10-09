package com.anqit.util.lamqa.trials;

public interface ExceptionHandler {
	void handle(Exception e);
	
	static ExceptionHandler NOOP = e -> {};
}
