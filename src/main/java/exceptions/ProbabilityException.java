package main.java.exceptions;

public class ProbabilityException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4575239864032885026L;

	public enum CODE {
		NEGATIVE(NEGATIVE_MESSAGE), BEYOND_RANGE(BEYOND_RANGE_MESSAGE);

		private String message;

		private CODE(String error) {
			this.message = error;
		}

		public String getMessage() {
			return message;
		}

	}

	private static final String NEGATIVE_MESSAGE = "The probability entered is negative";
	private static final String BEYOND_RANGE_MESSAGE = "The probability entered is beyond allowed range like its sum with other probabilites must be one.";

	public ProbabilityException(CODE error) {
		super(error.getMessage());
	}

}
