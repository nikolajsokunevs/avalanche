package lv.on.avalanche.exceptions;


public class GameException extends RuntimeException {
    private int statusCode;
    private String errorMessage;

    public GameException(int statusCode, String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}