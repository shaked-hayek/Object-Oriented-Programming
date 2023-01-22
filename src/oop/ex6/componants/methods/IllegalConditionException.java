package oop.ex6.componants.methods;

public class IllegalConditionException extends Exception {
    private static final String ERROR_MSG = "Illegal condition statement";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
