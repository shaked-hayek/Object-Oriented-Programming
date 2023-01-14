package oop.ex6.componants.variables;

public class InvalidVarDeclarationException extends Exception {
    private static final String ERROR_MSG = "Invalid variable declaration";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
