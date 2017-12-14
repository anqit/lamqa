package com.anqit.util.lamqa.function;

import java.util.function.Supplier;

/**
 * An extension to {@link Supplier} that can throw a checked {@link Exception}
 * 
 * @param <R>
 * 		 the type of results supplied by this supplier
 */
public interface ThrowingSupplier<R> extends Supplier<R>, Returns<R> {
	@Override
	default R get() {
		try {
			return getThrows(); 
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets a result, possibly throwing an {@link Exception}
	 * @return
	 * 		a result
	 * @throws Exception
	 */
	R getThrows() throws Exception;
}
