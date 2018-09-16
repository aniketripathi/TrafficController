package main.java.exceptions;

public class InvalidConfigException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1577300451193719815L;

	public static enum CODE {
		INVALID_STATEMENT(INVALID_STATEMENT_MESSAGE), INVALID_VALUE(INVALID_VALUE_MESSAGE);

		private String message;

		private CODE(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	public static final String INVALID_STATEMENT_MESSAGE = "Error reading config. The statements does not end in terminals.";
	public static final String INVALID_VALUE_MESSAGE = "Error reading config. One or more values of attributes have unexpected value.";

	public InvalidConfigException(String string) {
		super(string);
	}

}
