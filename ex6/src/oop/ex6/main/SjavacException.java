package oop.ex6.main;

public abstract class SjavacException extends Exception {
    /**
     * raises specific Exception
     * @param message specific message matching the Exception
     */
    public SjavacException(String message){
        super(message);
    }
}
