package oop.ex6.componants;

import oop.ex6.componants.methods.GlobalScope;
import oop.ex6.componants.methods.Method;
import oop.ex6.componants.methods.MethodDeclarationException;
import oop.ex6.componants.methods.Scope;
import oop.ex6.componants.variables.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineValidator {

    private static final Pattern VALID_END_REGEX = Pattern.compile(".*[;|}|{]\\s*");
    private static final Pattern VAR_LINE_END = Pattern.compile(".*;\\s*");
    private static final Pattern SCOPE_OPEN_LINE_END = Pattern.compile(".*\\{\\s*");
    private static final Pattern SCOPE_CLOSE_LINE_END = Pattern.compile("\\s*\\}\\s*");
    private HashMap<String, List<String>> methodContent;
    private int scopeOpenCounter;
    private int scopeCloseCounter;
    private GlobalScope globalScope;
    private String currentMethodName;

    private static final String SCOPE_END_EXCEPTION_MSG = "Scope ends in illegal manner";
    private static final String LINE_END_EXCEPTION_MSG = "Line ends with wrong char or invalid structure";
    private static final String SCOPE_DECLARATION_EXCEPTION_MSG = "Scope declaration is illegal";
    private static final String CONDITION_OUT_OF_METHOD_EXCEPTION_MSG = "Scope declaration out of method";

    public LineValidator(GlobalScope globalScope) {
        this.globalScope = globalScope;
        methodContent = new HashMap<>();
        scopeOpenCounter = 0;
        scopeCloseCounter = 0;
        currentMethodName = null;
    }

    public void validate(String line)
            throws InvalidVarTypeException, ValueMismatchException,
            InvalidVarDeclarationException, VarNameInitializedException, ScopeException,
            MethodDeclarationException, IllegalFinalVarAssigmentException, ScopeDeclarationException {
        if (!isRegexMatches(VALID_END_REGEX, line)) {
            throw new ScopeException(LINE_END_EXCEPTION_MSG);
        }
        if (isRegexMatches(VAR_LINE_END, line)) {
            handleVariableLine(line);

        } else if (isRegexMatches(SCOPE_OPEN_LINE_END, line)) {
            scopeOpenCounter++;
            line = line.replaceAll("\\{", "");
            if (Method.checkIsMethod(line)) {
                handleMethodDeclareLine(line);
            } else if (Scope.isValidScopeDeclaration(line)) {
                if (currentMethodName == null) {
                    throw new ScopeException(SCOPE_DECLARATION_EXCEPTION_MSG);
                }
            } else {
                throw new ScopeDeclarationException();
            }
        } else if (isEndOfScope(line)) {
            scopeCloseCounter++;
            if (scopeCloseCounter > scopeOpenCounter) {
                throw new ScopeException(SCOPE_END_EXCEPTION_MSG);
            } else if (scopeCloseCounter == scopeOpenCounter) {
                addLineToMethodContent(line);
                currentMethodName = null;
            }
        } else {
            throw new ScopeException(LINE_END_EXCEPTION_MSG);
        }
        addLineToMethodContent(line);
    }

    private void addLineToMethodContent(String line) {
        if (currentMethodName != null) {
            methodContent.get(currentMethodName).add(line);
        }
    }

    public void finalCheck() throws ScopeException {
        if (scopeOpenCounter != scopeCloseCounter) {
            throw new ScopeException(SCOPE_END_EXCEPTION_MSG);
        }
    }

    private static boolean isRegexMatches(Pattern pattern, String line) {
        Matcher m = pattern.matcher(line);
        return m.matches();
    }

    public static boolean isEndOfScope(String line) {
        return isRegexMatches(SCOPE_CLOSE_LINE_END, line);
    }

    public List<String> getMethodLines(String methodName) {
        return methodContent.get(methodName);
    }

    private void handleVariableLine(String line) throws IllegalFinalVarAssigmentException,
            InvalidVarTypeException, InvalidVarDeclarationException, VarNameInitializedException,
            ValueMismatchException {
        line = line.trim().replaceAll(";$","");
        if (currentMethodName == null) {
            Variables variables = new Variables(globalScope);
            variables.processVarsLine(line);
        }
    }

    private void handleMethodDeclareLine(String line) throws MethodDeclarationException,
            IllegalFinalVarAssigmentException, InvalidVarTypeException, VarNameInitializedException,
            ValueMismatchException {
        Method method = new Method(globalScope, line);
        if (!globalScope.addMethodToScopeMap(method)) {
            throw new MethodDeclarationException();
        }
        methodContent.put(method.getName(), new ArrayList<>());
        currentMethodName = method.getName();
    }

}
