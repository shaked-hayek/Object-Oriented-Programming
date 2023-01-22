package oop.ex6.componants;

public class InvalidEndOfScopeException extends Exception {
    private static final String ERROR_MSG = "Scope ends in illegal manner";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
