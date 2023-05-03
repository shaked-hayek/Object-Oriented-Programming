package oop.ex6.main;

import oop.ex6.components.scopes.GlobalScope;
import oop.ex6.components.scopes.Method;
import oop.ex6.components.scopes.MethodDeclarationException;
import oop.ex6.components.scopes.Scope;
import oop.ex6.components.variables.*;

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
    private static final String BLOCK_OUT_OF_METHOD_EXCEPTION_MSG = "Block declaration out of method";
    private static final String METHOD_NAME_EXCEPTION_MSG = "Method name already exists";

    /**
     * constructor
     * object goes over the code for the first time - keeps variables and methods declarations and params
     * @param globalScope the most upper scope in code
     */
    public LineValidator(GlobalScope globalScope) {
        this.globalScope = globalScope;
        methodContent = new HashMap<>();
        scopeOpenCounter = 0;
        scopeCloseCounter = 0;
        currentMethodName = null;
    }

    /**
     * helper to go over the code for the first time - keeps variables and methods declarations and params
     * makes sure every line is valid,
     * otherwise - raises specific exception
     * @param line to validate
     * @throws InvalidVarTypeException when variable type invalid
     * @throws VariableDeclarationException when the variable declaration is illegal
     * @throws ScopeException when scope's structure\syntax is invalid
     * @throws MethodDeclarationException when the method declaration is illegal
     * @throws VariableAssignmentException when the variable assignment is illegal
     */
    public void validate(String line)
            throws InvalidVarTypeException,
            VariableDeclarationException, ScopeException,
            MethodDeclarationException, VariableAssignmentException {
        if (!isRegexMatches(VALID_END_REGEX, line)) {
            throw new ScopeException(LINE_END_EXCEPTION_MSG);
        }
        if (isRegexMatches(VAR_LINE_END, line)) {
            line = handleVariableLine(line);

        } else if (isRegexMatches(SCOPE_OPEN_LINE_END, line)) {
            scopeOpenCounter++;
            line = line.replaceAll("\\{", "");
            if (Method.checkIsMethod(line)) {
                handleMethodDeclareLine(line);
            } else if (Scope.isValidScopeDeclaration(line)) {
                if (currentMethodName == null) {
                    throw new ScopeException(BLOCK_OUT_OF_METHOD_EXCEPTION_MSG);
                }
            } else {
                throw new ScopeException(SCOPE_DECLARATION_EXCEPTION_MSG);
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

    /**
     * adds a line to the list containing all the method's content lines (by method's name)
     * @param line representing part of method content - to be added to the list
     */
    private void addLineToMethodContent(String line) {
        if (currentMethodName != null) {
            methodContent.get(currentMethodName).add(line);
        }
    }

    /**
     * makes sure a scope has equal number of openers and closers "{", "}"
     * @throws ScopeException number of openers and closers is not the same at the end of code
     */
    public void finalCheck() throws ScopeException {
        if (scopeOpenCounter != scopeCloseCounter) {
            throw new ScopeException(SCOPE_END_EXCEPTION_MSG);
        }
    }

    /**
     * checks if a line fits line pattern provided
     * @param pattern the line should fit
     * @param line to check if fits
     * @return true if a line fits line pattern provided, else false
     */
    private static boolean isRegexMatches(Pattern pattern, String line) {
        Matcher m = pattern.matcher(line);
        return m.matches();
    }

    /**
     * checks if a scope ends in a valid way
     * @param line to be checked
     * @return true if a scope ends in a valid way as in "}", else false
     */
    public static boolean isEndOfScope(String line) {
        return isRegexMatches(SCOPE_CLOSE_LINE_END, line);
    }

    /**
     * gets all method's content by its name
     * @param methodName of method to get its content
     * @return a list containing all methods content (lines by their order)
     */
    public List<String> getMethodLines(String methodName) {
        return methodContent.get(methodName);
    }

    /**
     * goes over a line containing variable declaration\assignment and saves\changes them properly
     * @param line containing variable declaration\assignment to be handled
     * @return the rest of line (after handling the variables)
     * @throws InvalidVarTypeException when variable type invalid
     * @throws VariableDeclarationException when the variable declaration is illegal
     * @throws VariableAssignmentException when the variable assignment is illegal
     */
    private String handleVariableLine(String line) throws
            InvalidVarTypeException, VariableDeclarationException, VariableAssignmentException {
        line = line.trim().replaceAll(";$","");
        if (currentMethodName == null) {
            Variables variables = new Variables(globalScope);
            variables.processVarsLine(line);
        }
        return line;
    }

    /**
     * goes over a line containing method declaration and saves it properly
     * @param line containing method declaration to be handled
     * @throws MethodDeclarationException when the method declaration is illegal
     * @throws InvalidVarTypeException when variable type invalid
     * @throws VariableDeclarationException when the variable declaration is illegal
     * @throws VariableAssignmentException when the variable assignment is illegal
     */
    private void handleMethodDeclareLine(String line) throws MethodDeclarationException,
            InvalidVarTypeException, VariableDeclarationException, VariableAssignmentException {
        Method method = new Method(globalScope, line);
        if (!globalScope.addMethodToScopeMap(method)) {
            throw new MethodDeclarationException(METHOD_NAME_EXCEPTION_MSG);
        }
        methodContent.put(method.getName(), new ArrayList<>());
        currentMethodName = method.getName();
    }

}
