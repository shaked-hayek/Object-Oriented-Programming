package oop.ex6.componants.methods;

public class MethodVarInvalidException extends Exception {
    private static final String ERROR_MSG = "Illegal variable in method declaration";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
