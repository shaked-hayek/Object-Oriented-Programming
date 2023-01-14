package oop.ex6.componants.variables;

public class InvalidVarTypeException extends Exception {
    private static final String ERROR_MSG = "Invalid var type";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
