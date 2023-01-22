package oop.ex6.componants;

public class IllegalReturnStatement extends Exception {
    private static final String ERROR_MSG = "Return statement not in end of method";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}