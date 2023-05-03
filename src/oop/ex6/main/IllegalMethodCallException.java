package oop.ex6.main;


public class IllegalMethodCallException extends SjavacException {
    /**
     * raises specific Exception when calling illegal method
     * @param message "Illegal method call - method doesn't exist" or
     *                "Illegal method call - parameters doesn't match method's expected parameters"
     */
    public IllegalMethodCallException(String message){
        super(message);
    }
}
