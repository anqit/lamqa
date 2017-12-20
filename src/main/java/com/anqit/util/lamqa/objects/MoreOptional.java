package com.anqit.util.lamqa.objects;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * An {@link Optional} with... more options...
 *
 * @param <T> the type of the wrapped value
 */
public class MoreOptional<T> {
	private static final MoreOptional<?> EMPTY = new MoreOptional<>(Optional.empty());
	private final Optional<T> wrapped;
	
	private MoreOptional(T t) { this(Optional.of(t)); }
	private MoreOptional(Optional<T> optional) { wrapped = optional; }

	/**
	 * {@link Optional#of(Object)}
	 * @param t
	 * @return a MoreOptional wrapping the given value
	 * @throws NullPointerException if the value is null
	 */
	public static <T> MoreOptional<T> of(T t) {
		return new MoreOptional<>(t);
	}
	
	/**
	 * @param optional
	 * @return a MoreOptional wrapping the given Optional
	 */
	public static <T> MoreOptional<T> of(Optional<T> optional) {
		return optional.map(MoreOptional::new).orElse(empty());
	}
	
	/**
	 * {@link Optional#ofNullable(Object)}
	 * 
	 * @param t
	 * @return a MoreOptional wrapping the given value if non-null, otherwise
	 * 		an empty MoreOptional
	 */
	public static <T> MoreOptional<T> ofNullable(T t) {
		return t == null ? empty() : of(t);
	}
	
	/**
	 * {@link Optional#empty()}
	 * @return an empty MoreOptional
	 */
	@SuppressWarnings("unchecked")
	public static <T> MoreOptional<T> empty() {
		return (MoreOptional<T>) EMPTY;
	}
	
	/**
	 * This emulates {@link Optional#get()}. Returns the wrapped value if non-null,
	 * otherwise throws {@link NoSuchElementException}
	 * 
	 * @return the non-null value held by this MoreOptional
	 * @throws NoSuchElementException if no value is present
	 */
	public T get() {
		return wrapped.get();
	}
	
	/**
	 * Like {@link #get()}, this will return the wrapped value if present, or returns null 
	 * if not (instead of throwing {@link NoSuchElementException})
	 * 
	 * @return the possibly null value held by this MoreOptional
	 */
	public T getOrNull() {
		return wrapped.orElse(null);
	}
	
	/**
	 * {@link Optional#isPresent()}
	 * 
	 * @return {@code true} if the value is present, {@code false} otherwise
	 */
	public boolean isPresent() {
		return wrapped.isPresent();
	}
	
	/**
	 * Opposite of {@link #isPresent()}
	 * 
	 * @return {@code false} if the value is present, {@code true} otherwise
	 */
	public boolean isAbsent() {
		return !isPresent();
	}
	/**
	 * Similar to {@link Optional#ifPresent(Consumer)}, but returns this MoreOptional instance
	 *  
	 * @param consumer the {@link Consumer} to execute if a value is present
	 * @return this
	 */
	public MoreOptional<T> ifPresent(Consumer<? super T> consumer) {
		if(isPresent()) {
			consumer.accept(get());
		}
		
		return this;
	}
	
	/**
	 * Perform the given action if no value is present, otherwise do nothing
	 * Returns this MoreOptional instance
	 * 
	 * @param action the action to run
	 * @return this
	 */
	public MoreOptional<T> ifAbsent(Runnable action) {
		if(isAbsent()) {
			action.run();
		}
		
		return this;
	}

	/**
	 * If the value is present, invoke the given {@link Consumer} with that value, otherwise perform the {@link Runnable}
	 * @param consumer the block to run if the value is present
	 * @param action the block to run if the value is absent
	 * @return this
	 */
	public MoreOptional<T> ifPresentOrElse(Consumer<? super T> consumer, Runnable action) {
		ifPresent(consumer);
		return ifAbsent(action);
	}

	/**
	 * {@link Optional#filter(Predicate)}
	 * 
	 * @param predicate
	 * @return an Optional describing the value of this Optional if a value is present and the value matches the given predicate, otherwise an empty Optional
	 * @throws NullPointerException - if the predicate is null
	 */
	public MoreOptional<T> filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if(isAbsent()) {
			return this;
		} else {
			return predicate.test(get()) ? this : empty();
		}
	}
	
	/**
	 * {@link Optional#map(Function)}
	 * 
	 * @param mapper
	 * @return a MoreOptional describing the result of applying a mapping function to the value of this MoreOptional,
	 * 		if a value is present, otherwise an empty Optional
	 * @throws NullPointerException if the mapping function is null
	 */
	public <U> MoreOptional<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (isAbsent()) {
        		return empty();
        } else {
        		return of(wrapped.map(mapper));
        }
	}
	
	/**
	 * {@link Optional#flatMap(Function)}
	 * 
	 * @param mapper
	 * @return the result of applying a MoreOptional-bearing mapping function to the value of this MoreOptional,
	 * 		if a value is present, otherwise an empty MoreOptional
	 * @throws NullPointerException - if the mapping function is null or returns a null result
	 */
	@SuppressWarnings("unchecked")
	public <U> MoreOptional<U> flatMap(Function<? super T, ? extends MoreOptional<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        if (isAbsent())
            return empty();
        else {
            return (MoreOptional<U>) Objects.requireNonNull(mapper.apply(get()));
        }
    }
	
	/**
	 * {@link Optional#orElse(Object)}
	 * 
	 * @param other
	 * @return the value wrapped by this MoreOptional if present, otherwise the given value
	 */
	public T orElse(T other) {
		return isPresent() ? get() : other;
	}

	/**
	 * {@link Optional#orElseGet(Supplier)}
	 * 
	 * @param otherSupplier
	 * @return the value wrapped by this MoreOptional if present, otherwise the result of 
	 * 		invoking the given supplier
	 */
	public T orElseGet(Supplier<? extends T> otherSupplier) {
		return isPresent() ? get() : otherSupplier.get();
	}

	/**
	 * {@link Optional#orElseThrow(Supplier)}
	 * 
	 * @param exceptionSupplier
	 * @return the value wrapped by this MoreOptional if present
	 * @throws X if the value is not present
	 */
	public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isPresent()) {
            return get();
        } else {
            throw exceptionSupplier.get();
        }
    }
	
	/**
	 * Return this MoreOptional if its value is present, or the one from the passed in {@link Supplier}
	 * @param supplier
	 * @return this instance if the value is present, or the MoreOptional obtained from invoking
	 * 		the given {@link Supplier}
	 */
	@SuppressWarnings("unchecked")
	public MoreOptional<T> or(Supplier<? extends MoreOptional<? extends T>> supplier) {
		return isPresent() ? this : (MoreOptional<T>) supplier.get();
	}
	/**
	 * @return a stream of the wrapped value if present, or else an empty stream
	 */
	public Stream<T> stream() {
		return map(Stream::of).orElseGet(Stream::empty);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		
		if(!(obj instanceof MoreOptional)) return false;
		MoreOptional<?> other = (MoreOptional<?>) obj;

		return (Objects.equals(wrapped, other.wrapped));
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(wrapped);
	}
	
	@Override
	public String toString() {
		return map(v -> String.format("MoreOptional[%s]", v))
				.orElse("MoreOptional.empty");
	}	
}
