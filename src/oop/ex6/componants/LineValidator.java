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

    private static final String VALID_END_REGEX = ".*[;|}|{]\\s*";
    private static final String VAR_LINE_END = ".*;\\s*";
    private static final String SCOPE_OPEN_LINE_END = ".*\\{\\s*";
    private static final String SCOPE_CLOSE_LINE_END = ".*\\}\\s*";
    private HashMap<String, List<String>> methodContent;
    private int scopeOpenCounter;
    private int scopeCloseCounter;
    private GlobalScope globalScope;
    private String currentMethodName;

    public LineValidator(GlobalScope globalScope) {
        this.globalScope = globalScope;
        methodContent = new HashMap<>();
        scopeOpenCounter = 0;
        scopeCloseCounter = 0;
        currentMethodName = null;
    }

    public void validate(String line)
            throws InvalidLineEndException, InvalidVarTypeException, ValueMismatchException,
            InvalidVarDeclarationException, VarNameInitializedException, InvalidEndOfScopeException,
            MethodDeclarationException, IllegalFinalVarAssigmentException, ScopeDeclarationException {
        if (!isRegexMatches(line, VALID_END_REGEX)) {
            throw new InvalidLineEndException();
        }
        if (isRegexMatches(line, VAR_LINE_END)) {
            if (currentMethodName == null) {
                line = line.replaceAll(";", "");
                Variables variables = new Variables(globalScope);
                variables.processVarsLine(line);
            }

        } else if (isRegexMatches(line, SCOPE_OPEN_LINE_END)) {
            scopeOpenCounter++;
            line = line.replaceAll("\\{", "");
            if (Method.checkIsMethod(line)) {
                Method method = new Method(globalScope, line);
                if (!globalScope.addMethodToScopeMap(method)) {
                    throw new MethodDeclarationException();
                }
                methodContent.put(method.getName(), new ArrayList<>());
                currentMethodName = method.getName();
            } else if (Scope.isValidScopeDeclaration(line)) {
                // if / while declaration is ok
            } else {
                throw new ScopeDeclarationException();
            }
        } else if (isRegexMatches(line, SCOPE_CLOSE_LINE_END)) {
            scopeCloseCounter++;
            if (scopeCloseCounter > scopeOpenCounter) {
                throw new InvalidEndOfScopeException();
            } else if (scopeCloseCounter == scopeOpenCounter) {
                addLineToMethodContent(line);
                currentMethodName = null;
            }
        }
        addLineToMethodContent(line);
    }

    private void addLineToMethodContent(String line) {
        if (currentMethodName != null) {
            methodContent.get(currentMethodName).add(line);
        }
    }

    public void finalCheck() throws InvalidEndOfScopeException {
        if (scopeOpenCounter != scopeCloseCounter) {
            throw new InvalidEndOfScopeException();
        }
    }

    private static boolean isRegexMatches(String line, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        return m.matches();
    }

    public List<String> getMethodLines(String methodName) {
        return methodContent.get(methodName);
    }
}
