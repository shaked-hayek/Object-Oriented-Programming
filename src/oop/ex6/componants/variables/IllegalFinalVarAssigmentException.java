package oop.ex6.componants.variables;

public class IllegalFinalVarAssigmentException extends Exception {
    private static final String ERROR_MSG = "Illegal assigment to a final variable";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
