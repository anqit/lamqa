package com.anqit.util.lamqa.objects;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A container for a value that is lazily obtained from a provided {@link Supplier} function
 * and cached.
 * <p>
 * The {@link Supplier} is run only the first time the value is requested (using {@link #get()}). The result
 * is cached and returned by each subsequent call to {@link #get()}, even if the value obtained from the 
 * {@link Supplier} was null 
 *
 * @param <T> the type of the value
 */
public class LazyValue<T> {
	private T value;
	private Supplier<T> valueSupplier;
	private boolean evaluated = false;

	private LazyValue(T value) { this(() -> value); }
	private LazyValue(Supplier<T> valueSupplier) { this.valueSupplier = valueSupplier; }
	
	/**
	 * Create a {@link LazyValue} instance of the given value
	 * 
	 * @param t the value to wrap
	 * 
	 * @return a new {@link LazyValue} instance
	 */
	public static <T> LazyValue<T> of(T t) {
		return new LazyValue<>(t);
	}
	
	/**
	 * Create a {@link LazyValue} instance of the given {@link Supplier}
	 * 
	 * @param valueSupplier the {@link Supplier} function to call to obtain the value
	 * 
	 * @return a new {@link LazyValue} instance
	 */
	public static <T> LazyValue<T> of(Supplier<T> valueSupplier) {
		return new LazyValue<>(valueSupplier);
	}
	
	/**
	 * Get the value, calling the provided {@link Supplier} if the value has not been obtained yet
	 * 
	 * @return the obtained value
	 */
	public T get() {
		if(isNotEvaluated()) {
			value = invoke();
		}
		
		return value;
	}
	
	/**
	 * @return if the {@link Supplier} has been evaluated yet
	 */
	public boolean isEvaluated() {
		return evaluated;
	}
	
	/**
	 * @return if the {@link Supplier} has not been evaluated yet
	 */
	public boolean isNotEvaluated() {
		return !isEvaluated();
	}
	
	/**
	 * Refresh the value by re-invoking the {@link Supplier}
	 * 
	 * @return {@code true} if the refreshed value changed as a result of re-invoking the {@link Supplier}
	 * 		(determined by {@link Objects#equals(Object)}), {@code false} otherwise.
	 */
	public boolean refresh() {
		T newValue = invoke();
				
		boolean hasChanged = !Objects.equals(newValue, value);

		value = newValue;

		return hasChanged;
	}
	
	/**
	 * Update the {@link Supplier} used to obtain the value on subsequent calls to {@link #get()}.
	 * Resets the "evaluated" state, so that the next call to {@link #get()} will run the {@link Supplier}
	 * 
	 * @param newSupplier
	 * @return this instance
	 */
	public LazyValue<T> update(Supplier<T> newSupplier) {
		this.valueSupplier = newSupplier;
		evaluated = false;
		
		return this;
	}
	
	private T invoke() {
		T t = valueSupplier.get();
		evaluated = true;
		
		return t;
	}
}
