package com.anqit.util.lamqa.function;

import java.util.function.Function;

import com.anqit.util.lamqa.trials.Returns;

public interface ThrowingFunction<T, R> extends Function<T, R>, Returns<R> {
	@Override
	default R apply(T t) {
		try {
			return applyThrows(t);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	R applyThrows(T t) throws Exception;
}
