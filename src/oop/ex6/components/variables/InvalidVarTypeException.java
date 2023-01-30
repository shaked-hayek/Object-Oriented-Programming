package oop.ex6.components.variables;

import oop.ex6.main.SjavacException;

public class InvalidVarTypeException  extends SjavacException {
    /**
     * raises specific Exception when variable type invalid
     * @param message "Invalid var type"
     */
    public InvalidVarTypeException(String message){
        super(message);
    }
}
