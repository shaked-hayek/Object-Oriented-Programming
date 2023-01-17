package oop.ex6.componants;

import oop.ex6.componants.methods.GlobalScope;
import oop.ex6.componants.methods.Method;
import oop.ex6.componants.methods.MethodDeclarationException;
import oop.ex6.componants.variables.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineValidator {

    private static final String VALID_END_REGEX = ".*[;|}|{]\\s*";
    private static final String VAR_LINE_END = ".*;\\s*";
    private static final String SCOPE_OPEN_LINE_END = ".*\\{\\s*";
    private static final String SCOPE_CLOSE_LINE_END = ".*\\}\\s*";
    private int scopeOpenCounter;
    private int scopeCloseCounter;
    private GlobalScope globalScope;

    public LineValidator(GlobalScope globalScope) {
        this.globalScope = globalScope;
        scopeOpenCounter = 0;
        scopeCloseCounter = 0;
    }

    public void validate(String line)
            throws InvalidLineEndException, InvalidVarTypeException, ValueMismatchException,
            InvalidVarDeclarationException, VarNameInitializedException, InvalidEndOfScopeException, MethodDeclarationException, IllegalFinalVarAssigmentException {
        if (!isRegexMatches(line, VALID_END_REGEX)) {
            throw new InvalidLineEndException();
        }
        if (isRegexMatches(line, VAR_LINE_END)) {
            line = line.replaceAll(";", "");
            new Variables(line, globalScope);

        } else if (isRegexMatches(line, SCOPE_OPEN_LINE_END)) {
            scopeOpenCounter++;
            line = line.replaceAll("\\{", "");
            if (Method.checkIsMethod(line)) {
                Method method = new Method(globalScope, line);
                globalScope.addMethodToScopeMap(method);
            }

        } else if (isRegexMatches(line, SCOPE_CLOSE_LINE_END)) {
            scopeCloseCounter++;
            if (scopeCloseCounter > scopeOpenCounter) {
                throw new InvalidEndOfScopeException();
            }
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
}
