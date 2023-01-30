package oop.ex6.components.scopes;

import oop.ex6.main.SjavacException;

public class MethodDeclarationException extends SjavacException {
    /**
     * raises specific Exception when the method declaration is invalid
     * @param message "Illegal variable in method declaration" or "Illegal method declaration"
     */
    public MethodDeclarationException(String message){
        super(message);
    }
}
