package oop.ex6.components.scopes;

import oop.ex6.main.SjavacException;

public class IllegalConditionException extends SjavacException {
    /**
     * raises specific Exception when the condition in if\while is invalid
     * @param message "Illegal condition statement"
     */
    public IllegalConditionException(String message){
        super(message);
    }
}

