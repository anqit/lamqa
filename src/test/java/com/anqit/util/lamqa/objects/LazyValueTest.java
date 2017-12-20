package com.anqit.util.lamqa.objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.function.Supplier;

import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.Test;

public class LazyValueTest {

	private static final Integer VALUE1 = 13;
	private static final Integer VALUE2 = 9;

	private static final Supplier<Integer> STATIC_SUPPLIER = () -> {
		return VALUE1;
	};
	
	private int count = 0;
	private final Supplier<Integer> dynamicSupplier = () -> {
		return ++count;
	};
	
	@Test
	public void testStaticSupplier() {
		LazyValue<Integer> lazyInt = LazyValue.of(STATIC_SUPPLIER);
		assertIsNotEvaluated(lazyInt);

		Integer value = assertThatGetReturns(VALUE1, lazyInt);
		assertIsEvaluated(lazyInt);
		assertThatGetReturns(VALUE1, lazyInt);
		
		assertFalse(lazyInt.refresh());
		assertThatGetReturns(VALUE1, lazyInt);
	}
	
	@Test
	public void testDynamicSupplier() {
		LazyValue<Integer> lazyInt = LazyValue.of(dynamicSupplier);
		assertIsNotEvaluated(lazyInt);
		
		Integer value = assertThatGetReturns(1, lazyInt);
		assertIsEvaluated(lazyInt);
		assertThatGetReturns(1, lazyInt);

		assertTrue(lazyInt.refresh());
		
		assertThatGetReturns(2, lazyInt);
	}
	
	@Test
	public void testUpdateSupplier() {
		LazyValue<Integer> lazyInt = LazyValue.of(STATIC_SUPPLIER);
		assertIsNotEvaluated(lazyInt);
		
		Integer value = assertThatGetReturns(VALUE1, lazyInt);
		assertIsEvaluated(lazyInt);
		
		lazyInt.update(dynamicSupplier);
		assertIsNotEvaluated(lazyInt);

		assertThatGetReturns(1, lazyInt);
		assertIsEvaluated(lazyInt);
		assertThatGetReturns(1, lazyInt);
		
		assertTrue(lazyInt.refresh());
		assertThatGetReturns(2, lazyInt);
	}
	
	private <T> T assertThatGetReturns(T t, LazyValue<T> lazyVal) {
		T val = lazyVal.get();
		
		assertThat(val, is(t));
		
		return val;
	}
	
	
	private void assertIsEvaluated(LazyValue<?> lazyVal) {
		assertThat(lazyVal.isEvaluated(), is(true));
	}
	private void assertIsNotEvaluated(LazyValue<?> lazyVal) {
		assertThat(lazyVal.isNotEvaluated(), is(true));
	}
}
