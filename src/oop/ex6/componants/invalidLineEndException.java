package oop.ex6.componants;

public class invalidLineEndException extends Throwable {
    private static final String ERROR_MSG = "Line ends with wrong char";

    @Override
    public String toString() {
        String s = getClass().getName();
        return s + ": " + ERROR_MSG;
    }
}
