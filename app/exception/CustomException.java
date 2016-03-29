package exception;

/**
 * Created by bedux on 02/03/16.
 */
public class CustomException extends RuntimeException {

    private Exception e;
    private String message;

    public CustomException(Exception e) {
        System.err.println(e.getMessage());
        this.e = e;
    }

    public CustomException() {
        System.err.println("My Errror");

    }

    public CustomException(String message) {
        this.message = message;
    }

    public Exception getException() {
        return e;
    }
}
