package com.anqit.util.lamqa.trials;

import java.util.Arrays;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;

import com.anqit.util.lamqa.function.ThrowingBiFunction;

//import org.junit.jupiter.api.Test;

import com.anqit.util.lamqa.trials.TrialAndError.BiFunctionalTrialAndError;

public class TrialAndErrorTest {
	int[][] divisionTests = { { 4, 2 }, { 2, 4 }, { -5, 2 }, { 3, -2 }, { 0, 2 }, { 2, 0 } };

	ThrowingBiFunction<Integer, Integer, Number> toDivideTheArgs = (dividend, divisor) -> dividend / divisor;
	Supplier<Number> justReturnZero = () -> 0;

	BiFunctionalTrialAndError<Integer, Integer, Number> divisionTrier;

	@Before
	public void setup() {
		divisionTrier = TrialAndError.firstTry(toDivideTheArgs);
	}

	@Test
	public void testDivisionWithoutDefault() {
		// returns null when divide by 0
		runTheDivisionTests();
	}

	@Test
	public void testWithDefault() {
		divisionTrier.ifAllElseFails(justReturnZero);

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
