package com.anqit.util.lamqa.function;

import java.util.function.BiFunction;

import com.anqit.util.lamqa.trials.Returns;

@FunctionalInterface
public interface ThrowingBiFunction<T, U, R> extends BiFunction<T, U, R>, Returns<R> {
	default R apply(T t, U u) {
		try {
			return applyThrows(t, u);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	R applyThrows(T t, U u) throws Exception;
}
