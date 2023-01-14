package oop.ex6.componants.methods;

public class MethodDeclarationException extends Exception {
    private static final String ERROR_MSG = "Illegal method declaration";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
