package com.anqit.util.lamqa.trials;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.anqit.util.lamqa.function.ThrowingBiFunction;
import com.anqit.util.lamqa.function.ThrowingFunction;

public abstract class TrialAndError<TE extends TrialAndError<TE, F, R>, F extends Returns<R>, R> {
	protected List<F> trials = new ArrayList<>();
	protected List<ExceptionHandler> handlers = new ArrayList<>();
	protected Supplier<R> defaultSupplier;
			
	protected TrialAndError(F firstAttempt, ExceptionHandler handler) {
		trials.add(firstAttempt);
		handlers.add(handler);
	}
	
	public static <R> NoArgTrialAndError<R> firstTry(Trial<R> firstAttempt) {
		return firstTry(firstAttempt, ExceptionHandler.NOOP);
	}
	
	public static <R> NoArgTrialAndError<R> firstTry(Trial<R> firstAttempt, ExceptionHandler handler) {
		return new NoArgTrialAndError<>(firstAttempt, handler);
	}

	public static <T, R> FunctionalTrialAndError<T, R> firstTry(ThrowingFunction<T, R> firstAttempt) {
		return firstTry(firstAttempt, ExceptionHandler.NOOP);
	}
	
	public static <T, R> FunctionalTrialAndError<T, R> firstTry(ThrowingFunction<T, R> firstAttempt, ExceptionHandler handler) {
		return new FunctionalTrialAndError<>(firstAttempt, handler);
	}

	public static <T, U,R> BiFunctionalTrialAndError<T, U, R> firstTry(ThrowingBiFunction<T, U,R> firstAttempt) {
		return firstTry(firstAttempt, ExceptionHandler.NOOP);
	}
	
	public static <T, U,R> BiFunctionalTrialAndError<T, U, R> firstTry(ThrowingBiFunction<T, U, R> firstAttempt, ExceptionHandler handler) {
		return new BiFunctionalTrialAndError<>(firstAttempt, handler);
	}

	public TrialAndError<TE, F, R> orElseTry(F anotherAttempt) {
		return orElseTry(anotherAttempt, ExceptionHandler.NOOP);
	}

	public TrialAndError<TE, F, R> orElseTry(F anotherAttempt, ExceptionHandler handler) {
		trials.add(anotherAttempt);
		handlers.add(handler);

		return (TE) this;
	}

	public TrialAndError<TE, F, R> ifNothingElse(R defaultValue) {
		return ifNothingElse(() -> defaultValue);
	}
	
	public TrialAndError<TE, F, R> ifNothingElse(Supplier<R> defaultSupplier) {
		this.defaultSupplier = defaultSupplier;
		
		return (TE) this;
	}

	public static void failTrial(String message) throws TrialFailedException {
		throw new TrialFailedException(message);
	}

	protected R runTrial(Function<F, Trial<R>> toTrial) {
		for(int i = 0; i < trials.size(); i++) {
			try {
				return toTrial.apply(trials.get(i)).doTrial();
			} catch (Exception e) {
				handlers.get(i).handle(e);
				continue;
			}
		}
		
		return defaultSupplier.get();
	}
	
	public static class NoArgTrialAndError<R> extends TrialAndError<NoArgTrialAndError<R>, Trial<R>, R> {
		protected NoArgTrialAndError(Trial<R> firstAttempt, ExceptionHandler handler) {
			super(firstAttempt, handler);
		}
		
		public R run() {
			return runTrial(Function.identity());
		}
	}
	
	public static class FunctionalTrialAndError<T, R> extends TrialAndError<FunctionalTrialAndError<T, R>, ThrowingFunction<T, R>, R> {
		private FunctionalTrialAndError(ThrowingFunction<T, R> firstAttempt, ExceptionHandler handler) {
			super(firstAttempt, handler);
		}

		public R on(T t) {
			return runTrial(f -> () -> f.apply(t));
		}
	}
	
	public static class BiFunctionalTrialAndError<T, U, R> extends TrialAndError<BiFunctionalTrialAndError<T, U, R>, ThrowingBiFunction<T, U, R>, R> {
		private BiFunctionalTrialAndError(ThrowingBiFunction<T, U, R> firstAttempt, ExceptionHandler handler) {
			super(firstAttempt, handler);
		}

		public R on(T t, U u) {
			return runTrial(f -> () -> f.apply(t, u));
		}
	}
}
