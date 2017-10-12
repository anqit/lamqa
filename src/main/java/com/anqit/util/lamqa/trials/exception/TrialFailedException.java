package com.anqit.util.lamqa.trials.exception;

import com.anqit.util.lamqa.trials.TrialAndError;

/**
 * An Exception indicating that a trial in a {@link TrialAndError} run has failed
 */
public class TrialFailedException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * create a {@link TrialFailedException} with the given message
	 * @param message
	 */
	public TrialFailedException(String message) {
		super(message);
	}
}
