package oop.ex6.componants;

import oop.ex6.componants.methods.GlobalScope;
import oop.ex6.componants.methods.Method;
import oop.ex6.componants.methods.Scope;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodValidator {
    private static final String PARENTHESES_REGEX = "\\((.*)\\)";
    private static final String METHOD_CALL_REGEX = "\\s*(\\S+)\\s*" + PARENTHESES_REGEX + "\\s*";
    private static final Pattern METHOD_CALL_PATTERN =  Pattern.compile(METHOD_CALL_REGEX);
    private final List<String> methodLines;
    private int currentLine;
    private GlobalScope globalScope;

    public MethodValidator(GlobalScope globalScope, LineValidator lv, String methodName) {
        this.globalScope = globalScope;
        methodLines = lv.getMethodLines(methodName);
        currentLine = 0;
    }

    public void validate() throws IllegalMethodCallException {
        methodLines.remove(0);
        for (String line : methodLines) {
            currentLine++;
            // TODO
            if (Scope.isValidScopeDeclaration(line)) {
                // if / while
            } else if (isMethodCall(line)) {
                checkMethodCall(line);
            }
        }
    }

    private boolean isMethodCall(String line) {
        Matcher m = METHOD_CALL_PATTERN.matcher(line);
        return m.matches();
    }

    private void checkMethodCall(String line) throws IllegalMethodCallException {
        Matcher m = METHOD_CALL_PATTERN.matcher(line);
        m.matches();
        String methodName = m.group(1);
        Method methodCalled = globalScope.getMethodFromMap(methodName);
        if (methodCalled == null) {
            throw new IllegalMethodCallException();
        }
        String methodVars = m.group(2);
    }

    public int getCurrentLine() {
        return currentLine;
    }
}
