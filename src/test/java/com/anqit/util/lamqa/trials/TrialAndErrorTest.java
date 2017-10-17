package com.anqit.util.lamqa.trials;

import java.util.Arrays;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.anqit.util.lamqa.function.ThrowingBiFunction;
import com.anqit.util.lamqa.trials.TrialAndError.BiFunctionalTrialAndError;

public class TrialAndErrorTest {
	int[][] divisionTests = { { 4, 2 }, { 2, 4 }, { -5, 2 }, { 3, -2 }, { 0, 2 }, { 2, 0 } };

	ThrowingBiFunction<Integer, Integer, Number> toDivideTheArgs = (dividend, divisor) -> dividend / divisor;
	Supplier<Number> justReturnZero = () -> 0;

	BiFunctionalTrialAndError<Integer, Integer, Number> divisionTrier;

	@Test
	public void testDivisionWithoutDefault() {
		// returns null when divide by 0
		divisionTrier = TrialAndError.firstTry(toDivideTheArgs);

		runTheDivisionTests();
	}

	@Test
	public void testWithDefault() {
		divisionTrier = TrialAndError.firstTry(toDivideTheArgs).ifAllElseFails(justReturnZero);

		runTheDivisionTests();
	}

	@Test
	public void testWithDefaultAndExceptionHandler() {
		divisionTrier = TrialAndError.firstTry(toDivideTheArgs, (e, t, u) -> {
			System.out.println(String.format("Caught exception %s while trying to divide %s by %s", e, t, u));
		}).ifAllElseFails(justReturnZero);

		runTheDivisionTests();
	}

	private void runTheDivisionTests() {
		Arrays.asList(divisionTests).stream().forEach(pair -> {
			int t = pair[0], u = pair[1];
			Number quotient = divisionTrier.on(t, u);
			System.out.println(String.format("%2d ÷ %2d = %2d", t, u, quotient));
		});
	}
}
