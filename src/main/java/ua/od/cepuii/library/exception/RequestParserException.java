package ua.od.cepuii.library.exception;

public class RequestParserException extends RuntimeException {
    public RequestParserException(String message) {
        super(message);
    }

    public RequestParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
