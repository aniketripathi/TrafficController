package exceptions;

public class ProbabilityException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4575239864032885026L;
	
	public enum ERROR {
		NEGATIVE(NEGATIVE_MESSAGE), BEYOND_RANGE(BEYOND_RANGE_MESSAGE);
		
		private String  message; 
		private ERROR(String error) {
			this.message = error;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String error) {
			this.message = error;
		}
		
		
		
	}
	
	private static final String NEGATIVE_MESSAGE = "The probability entered is negative";
	private static final String BEYOND_RANGE_MESSAGE = "The probability entered is beyond allowed range like its sum with other probabilites must be one.";
	
	public ProbabilityException(ERROR error) {
		super(error.getMessage());
	}
	
	
}
