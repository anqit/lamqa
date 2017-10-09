package com.anqit.util.lamqa.function;

public interface ThrowingVarFunction<R> {
	R apply(Object... objects) throws Exception;
}
