package oop.ex6.componants;

public class InvalidEndOfScopeException extends Exception {
    private static final String ERROR_MSG =
            "Scope ends in illegal manner - without return statement or with opening scope";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
