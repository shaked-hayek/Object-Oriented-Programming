package oop.ex6.componants;

public class ScopeDeclarationException extends Exception {
    private static final String ERROR_MSG = "Scope (if / while) declaration is illegal";

    @Override
    public String getMessage() {
        return ERROR_MSG;
    }
}
