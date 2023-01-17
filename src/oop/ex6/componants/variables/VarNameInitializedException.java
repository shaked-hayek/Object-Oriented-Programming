package oop.ex6.componants.variables;

public class VarNameInitializedException extends Exception {
    private static final String ERROR_MSG = "Variable initialization with illegal name";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
