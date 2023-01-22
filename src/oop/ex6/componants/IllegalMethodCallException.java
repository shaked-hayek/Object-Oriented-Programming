package oop.ex6.componants;

public class IllegalMethodCallException extends Exception {
    private static final String ERROR_MSG = "Illegal method call - method doesn't exist";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
