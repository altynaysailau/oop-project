package exceptions;

public class InvalidCitationsException extends Exception {
    
	private static final long serialVersionUID = 1L;

	public InvalidCitationsException(String message) {
        super(message);
    }
}