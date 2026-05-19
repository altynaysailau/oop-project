package exceptions;

/**
 * Thrown when a student attempts to fail (score < 50) a course
 * more than 3 times in total.
 */
public class MaxFailsExceededException extends Exception {
    public MaxFailsExceededException(String message) {
        super(message);
    }
}
