package com.anqit.util.lamqa.trials;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.anqit.util.lamqa.function.Accepts;
import com.anqit.util.lamqa.function.Returns;
import com.anqit.util.lamqa.function.ThrowingBiFunction;
import com.anqit.util.lamqa.function.ThrowingFunction;
import com.anqit.util.lamqa.function.ThrowingSupplier;
import com.anqit.util.lamqa.trials.exception.BiFunctionExceptionHandler;
import com.anqit.util.lamqa.trials.exception.ExceptionHandler;
import com.anqit.util.lamqa.trials.exception.FunctionExceptionHandler;
import com.anqit.util.lamqa.trials.exception.TrialFailedException;

/**
 * A framework to support a trial-and-error strategy for obtaining a value. Instances of {@link TrialAndError} 
 * set up an ordered chain of methods to call. Each method is attempted in turn to obtain a value of type <code>R</code>.
 * If the calling method throws any type of {@link Exception}, the {@link Exception} is processed by any associated 
 * {@link ExceptionHandler}, and the next method is attempted. Evaluating the functions in the chain continues until a
 * value is returned.
 * 
 * If all of the functions in the function chain throw, then the default value is returned if specified by either
 * {@link #ifAllElseFails(Object)} or {@link #ifAllElseFails(Supplier)}, or null.
 *
 * @param <TE>
 * 		The type of TrialAndError, used to support fluency
 * @param <F>
 * 		The type of the functions run by the trial
 * @param <E>
 * 		The type of the exception handler
 * @param <R>
 * 		The return type of the trial
 */
public abstract class TrialAndError<TE extends TrialAndError<TE, F, E, R>, F extends Returns<R>, E extends Accepts<Exception>, R> {
	private List<F> trials = new ArrayList<>();
	private List<E> handlers = new ArrayList<>();
	private Supplier<R> defaultSupplier = () -> null;
			
	private TrialAndError(F firstAttempt, E handler) {
		trials.add(firstAttempt);
		handlers.add(handler);
	}
	
	/**
	 * Creates a {@link NoArgTrialAndError} instance by specifying the first no-arg function to try. By not specifying
	 * an {@link ExceptionHandler}, any {@link Exception} thrown by this function will not be processed.
	 * 
	 * @param firstAttempt
	 * 		the first function to try
	 * @return
	 * 		a new {@link NoArgTrialAndError} instance
	 */
	public static <R> NoArgTrialAndError<R> firstTry(ThrowingSupplier<R> firstAttempt) {
		return firstTry(firstAttempt, ExceptionHandler.NOOP);
	}
	
	/**
	 * Creates a {@link NoArgTrialAndError} instance by specifying the first no-arg function to try, and its associated
	 * {@link ExceptionHandler}
	 * 
	 * @param firstAttempt
	 * 		the first function to try
	 * @param handler
	 * 		the {@link ExceptionHandler} to call if this attempt throws an {@link Exception}
	 * 		
	 * @return
	 * 		a new {@link NoArgTrialAndError} instance
	 */
	public static <R> NoArgTrialAndError<R> firstTry(ThrowingSupplier<R> firstAttempt, ExceptionHandler handler) {
		return new NoArgTrialAndError<>(firstAttempt, handler);
	}

	/**
	 * Creates a {@link FunctionalTrialAndError} instance by specifying the first single-arg function to try.
	 * By not specifying an {@link ExceptionHandler}, any {@link Exception} thrown by this function will not be processed.
	 * 
	 * @param firstAttempt
	 * 		the first function to try
	 * @return
	 * 		a new {@link FunctionalTrialAndError} instance
	 */
	public static <T, R> FunctionalTrialAndError<T, R> firstTry(ThrowingFunction<T, R> firstAttempt) {
		return firstTry(firstAttempt, FunctionExceptionHandler.NOOP());
	}
	
	/**
	 * Creates a {@link FunctionalTrialAndError} instance by specifying the first single-arg function to try, and its
	 * associated {@link ExceptionHandler}
	 * 
	 * @param firstAttempt
	 * 		the first function to try
	 * @param handler
	 * 		the {@link ExceptionHandler} to call if this attempt throws an {@link Exception}
	 * 		
	 * @return
	 * 		a new {@link FunctionalTrialAndError} instance
	 */
	public static <T, R> FunctionalTrialAndError<T, R> firstTry(ThrowingFunction<T, R> firstAttempt,
			FunctionExceptionHandler<T> handler) {
		return new FunctionalTrialAndError<>(firstAttempt, handler);
	}

	/**
	 * Creates a {@link BiFunctionalTrialAndError} instance by specifying the first single-arg function to try.
	 * By not specifying an {@link ExceptionHandler}, any {@link Exception} thrown by this function will not be processed.
	 * 
	 * @param firstAttempt
	 * 		the first function to try
	 * @return
	 * 		a new {@link BiFunctionalTrialAndError} instance
	 */
	public static <T, U, R> BiFunctionalTrialAndError<T, U, R> firstTry(ThrowingBiFunction<T, U, R> firstAttempt) {
		return firstTry(firstAttempt, BiFunctionExceptionHandler.NOOP());
	}
	
	/**
	 * Creates a {@link BiFunctionalTrialAndError} instance by specifying the first two-arg function to try, and its
	 * associated {@link ExceptionHandler}
	 * 
	 * @param firstAttempt
	 * 		the first function to try
	 * @param handler
	 * 		the {@link ExceptionHandler} to call if this attempt throws an {@link Exception}
	 * 		
	 * @return
	 * 		a new {@link BiFunctionalTrialAndError} instance
	 */
	public static <T, U, R> BiFunctionalTrialAndError<T, U, R> firstTry(ThrowingBiFunction<T, U, R> firstAttempt,
			BiFunctionExceptionHandler<T, U> handler) {
		return new BiFunctionalTrialAndError<>(firstAttempt, handler);
	}

	/**
	 * Add a function to this {@link TrialAndError}'s function chain. By not specifying an {@link ExceptionHandler}, 
	 * any {@link Exception} thrown by this function will not be processed.
	 * 
	 * @param anotherAttempt
	 * 		the function to add to the function chain
	 * @return
	 * 		this {@link TrialAndError} instance
	 */
	public TE orElseTry(F anotherAttempt) {
		return orElseTry(anotherAttempt, getNoOpHandler());
	}

	/**
	 * Add a function and its associated {@link ExceptionHandler} to this {@link TrialAndError}'s function chain
	 * 
	 * @param anotherAttempt
	 * 		the function to add to the function chain
	 * @param handler 
	 * 		the {@link ExceptionHandler} to call if this function throws an Exception
	 * @return
	 * 		this {@link TrialAndError} instance
	 */
	@SuppressWarnings("unchecked")
	public TE orElseTry(F anotherAttempt, E handler) {
		trials.add(anotherAttempt);
		handlers.add(handler);

		return (TE) this;
	}

	/**
	 * Set the default value to return if all of the functions in the function chain throw an {@link Exception}
	 * 
	 * @param defaultValue
	 * 		the value to return
	 * @return
	 * 		this {@link TrialAndError} instance	
	 */
	public TE ifAllElseFails(R defaultValue) {
		return ifAllElseFails(() -> defaultValue);
	}
	
	/**
	 * Set the {@link Supplier} to use to return a value if all of the functions in the function chain throw an 
	 * {@link Exception}
	 * 
	 * @param defaultSupplier
	 * 		a {@link Supplier} of type <code>R</code>
	 * 
	 * @return
	 * 		this {@link TrialAndError} instance	
	 */
	@SuppressWarnings("unchecked")
	public TE ifAllElseFails(Supplier<R> defaultSupplier) {
		this.defaultSupplier = defaultSupplier;
		
		return (TE) this;
	}
	
	protected abstract E getNoOpHandler();

	/**
	 * Helper method to call from functions in the function chain to fail the trial by throwing a 
	 * {@link TrialFailedException} with the given message
	 * 
	 * @param message
	 * 		the detail message of the {@link TrialFailedException} to be thrown
	 * 
	 * @throws TrialFailedException
	 */
	public static void failTrial(String message) throws TrialFailedException {
		throw new TrialFailedException(message);
	}

	protected R runTrial(Function<F, ThrowingSupplier<R>> toTrial, Function<E, ExceptionHandler> toHandler) {
		for(int i = 0; i < trials.size(); i++) {
			try {
				return toTrial.apply(trials.get(i)).getThrows();
			} catch (Exception e) {
				toHandler.apply(handlers.get(i)).handle(e);
				continue;
			}
		}
		
		return defaultSupplier.get();
	}
	
	/**
	 * A {@link TrialAndError} whose functions take no args
	 *
	 * @param <R>
	 * 		the return type of the trial
	 */
	public static class NoArgTrialAndError<R> extends TrialAndError<NoArgTrialAndError<R>, ThrowingSupplier<R>, ExceptionHandler, R> {
		protected NoArgTrialAndError(ThrowingSupplier<R> firstAttempt, ExceptionHandler handler) {
			super(firstAttempt, handler);
		}
		
		/**
		 * Execute the trial
		 * 
		 * @return
		 * 		the result obtained by the trial
		 */
		public R run() {
			return runTrial(Function.identity(), Function.identity());
		}

		@Override
		protected ExceptionHandler getNoOpHandler() {
			return ExceptionHandler.NOOP;
		}
	}
	
	/**
	 * A {@link TrialAndError} whose functions take a single arg
	 * @param <T> 
	 *		the type of first parameter of the functions
	 * @param <R>
	 * 		the return type of the trial
	 */
	public static class FunctionalTrialAndError<T, R> extends TrialAndError<FunctionalTrialAndError<T, R>, ThrowingFunction<T, R>, FunctionExceptionHandler<T>, R> {
		private FunctionalTrialAndError(ThrowingFunction<T, R> firstAttempt, FunctionExceptionHandler<T> handler) {
			super(firstAttempt, handler);
		}

		/**
		 * Execute the trial on the given input
		 * 
		 * @param t 
		 * 		the value to pass to the functions in the trial
		 * @return
		 * 		the result obtained by the trial
		 */
		public R on(T t) {
			return runTrial(f -> () -> f.applyThrows(t), h -> (e) -> h.handle(e, t));
		}

		@Override
		protected FunctionExceptionHandler<T> getNoOpHandler() {
			return FunctionExceptionHandler.NOOP();
		}
	}
	
	
	/**
	 * A {@link TrialAndError} whose functions take two args
	 * 
	 * @param <T> 
	 *		the type of first parameter of the functions
	 * @param <U> 
	 *		the type of second parameter of the functions 
	 * @param <R>
	 * 		the return type of the trial
	 */
	public static class BiFunctionalTrialAndError<T, U, R> extends TrialAndError<BiFunctionalTrialAndError<T, U, R>, ThrowingBiFunction<T, U, R>, BiFunctionExceptionHandler<T, U>, R> {
		private BiFunctionalTrialAndError(ThrowingBiFunction<T, U, R> firstAttempt, BiFunctionExceptionHandler<T, U> handler) {
			super(firstAttempt, handler);
		}

		/**
		 * Execute the trial on the given inputs
		 * 
		 * @param t 
		 * 		the value to pass as the first argument to the functions in the trial
		 * @param u  
		 * 		the value to pass as the second argument to the functions in the trial
		 * 
		 * @return
		 * 		the result obtained by the trial
		 */
		public R on(T t, U u) {
			return runTrial(f -> () -> f.applyThrows(t, u), h -> (e) -> h.handle(e, t, u));
		}

		@Override
		protected BiFunctionExceptionHandler<T, U> getNoOpHandler() {
			return BiFunctionExceptionHandler.NOOP();
		}
	}
}
