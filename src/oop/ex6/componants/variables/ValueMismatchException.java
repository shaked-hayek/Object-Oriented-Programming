package oop.ex6.componants.variables;

public class ValueMismatchException extends Exception {
    private static final String ERROR_MSG =
            "Value assigment failed - value doesn't match type declared or not initialized";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
