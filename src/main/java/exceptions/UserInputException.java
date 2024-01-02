package exceptions;

public class UserInputException extends RuntimeException {

    public UserInputException(String message) {
        super(message);
    }

    public UserInputException(String message, Throwable cause) {
        super(message, cause);
    }
}