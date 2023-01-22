package oop.ex6.componants;

public class IllegalVarInMethodCallException  extends Exception {
    private static final String ERROR_MSG =
            "Illegal method call - parameters doesn't match method's expected parameters";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
