package oop.ex6.componants;

public class invalidLineEndException extends Throwable {
    private static final String ERROR_MSG = "Line ends with wrong char";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
