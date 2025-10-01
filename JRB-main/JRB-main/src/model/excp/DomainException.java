package model.excp;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
