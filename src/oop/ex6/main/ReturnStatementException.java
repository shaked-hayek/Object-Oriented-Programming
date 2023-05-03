package oop.ex6.main;

public class ReturnStatementException extends SjavacException {
    /**
     * raises specific Exception when return statement is missing or invalid
     * @param message "Illegal return statement" or "Method ended without return statement"
     */
    public ReturnStatementException(String message){
        super(message);
    }
}