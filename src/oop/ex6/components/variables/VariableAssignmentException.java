package oop.ex6.components.variables;

import oop.ex6.main.SjavacException;

public class VariableAssignmentException extends SjavacException {
    /**
     * raises specific Exception when variable assignment illegal
     * @param message "Variable assignment is illegal" or "Variable assignment to unknown variable"
     *                or "Final variable declaration without initialization" or
     *                "Variable assignment to unknown variable" or "Illegal assigment to a final variable"
     */
    public VariableAssignmentException(String message){
        super(message);
    }
}
