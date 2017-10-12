package com.anqit.util.lamqa.function;

import java.util.function.Supplier;

public interface ThrowingSupplier<R> extends Supplier<R>, Returns<R> {
	@Override
	default R get() {
		try {
			return getThrows(); 
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	R getThrows() throws Exception;
}
