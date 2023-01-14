package oop.ex6.componants.variables;

public class invalidVarTypeException extends Throwable {
    private static final String ERROR_MSG = "Invalid var type";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
