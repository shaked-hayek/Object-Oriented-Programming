package oop.ex6.componants.methods;

import oop.ex6.main.SjavacException;

public class IllegalConditionException extends SjavacException {
    /**
     * raises specific Exception when the condition is invalid
     * @param message "Illegal condition statement"
     */
    public IllegalConditionException(String message){
        super(message);
    }
}

