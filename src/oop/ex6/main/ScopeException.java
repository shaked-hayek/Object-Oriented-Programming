package oop.ex6.main;

public class ScopeException extends SjavacException {
    /**
     * raises specific Exception when scope's structure\syntax is invalid
     * @param message "Line ends with wrong char or invalid structure" or "Scope ends in illegal manner"
     *                or "Scope declaration is illegal" or "Block declaration out of method"
     */
    public ScopeException(String message){
        super(message);
    }
}
