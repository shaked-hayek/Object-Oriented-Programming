package oop.ex6.componants.variables;

public class ValueTypeMismatchException extends Exception {
    private static final String ERROR_MSG = "Value doesn't match type declared";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
