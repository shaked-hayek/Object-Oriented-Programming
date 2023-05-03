package oop.ex6.components.variables;

import oop.ex6.main.SjavacException;

public class VariableDeclarationException extends SjavacException {
    /**
     * raises specific Exception when the variable declaration is illegal
     * @param message "Illegal Variable initialization" or "Illegal variable declaration" or
     *                "Variable initialization with illegal name" or "No variables in declaration" or
     *                "Too many commas in declaration"
     */
    public VariableDeclarationException(String message){
        super(message);
    }
}
