package oop.ex6.main;

import oop.ex6.componants.scopes.GlobalScope;
import oop.ex6.componants.scopes.IllegalConditionException;
import oop.ex6.componants.scopes.Method;
import oop.ex6.componants.scopes.Scope;
import oop.ex6.componants.variables.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static oop.ex6.componants.scopes.Scope.isValidCondition;

public class MethodValidator {
    private static final String METHOD_CALL_EXCEPTION_MSG = "Illegal method call - method doesn't exist";
    private static final String NO_RETURN_EXCEPTION_MSG = "Method ended without return statement";
    private static final String ILLEGAL_RETURN_EXCEPTION_MSG = "Illegal return statement";
    private static final String ILLEGAL_METHOD_CALL_EXCEPTION_MSG =
            "Illegal method call - parameters doesn't match method's expected parameters";

    private static final String PARENTHESES_REGEX = "\\((.*)\\)";
    private static final String METHOD_CALL_REGEX = "\\s*(\\S+)\\s*" + PARENTHESES_REGEX + "\\s*";
    private static final Pattern METHOD_CALL_PATTERN =  Pattern.compile(METHOD_CALL_REGEX);
    private static final Pattern RETURN_PATTERN =  Pattern.compile("\\s*return\\s*");
    private final List<String> methodLines;
    private final Method method;
    private Scope currentScope;
    private GlobalScope globalScope;

    public MethodValidator(GlobalScope globalScope, LineValidator lv, String methodName) {
        method = globalScope.getMethodFromMap(methodName);
        this.globalScope = globalScope;
        methodLines = lv.getMethodLines(methodName);
        currentScope = method;
    }

    public void validate()
            throws IllegalMethodCallException, ReturnStatementException, IllegalConditionException,
            InvalidVarTypeException, VariableDeclarationException, VariableAssignmentException {
        methodLines.remove(0);
        int currentLine = 0;
        for (String line : methodLines) {
            currentLine++;

            // Check return
            if (isReturn(line)) {
                if (currentLine == methodLines.size() - 1) {
                    // Check scope is global and next line is end
                    if ((currentScope != method) ||
                            (!LineValidator.isEndOfScope(methodLines.get(currentLine)))) {
                        throw new ReturnStatementException(ILLEGAL_RETURN_EXCEPTION_MSG);
                    } else {
                        return;
                    }
                }
                continue;
            }

            if (LineValidator.isEndOfScope(line)) { // }
                if (currentScope == method) {
                    throw new ReturnStatementException(NO_RETURN_EXCEPTION_MSG);
                }
                currentScope = currentScope.getParentScope();
            } else if (Scope.isValidScopeDeclaration(line)) { // while / if
                isValidCondition(line, currentScope);
                currentScope = new Scope(currentScope);
            } else if (isMethodCall(line)) {    // method call
                checkMethodCall(line);
            } else {
                Variables variables = new Variables(currentScope);
                variables.processVarsLine(line);
            }
        }
        // If got here there was no return statement
        throw new ReturnStatementException(NO_RETURN_EXCEPTION_MSG);
    }

    private boolean isReturn(String line) {
        Matcher m = RETURN_PATTERN.matcher(line);
        return m.matches();
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
            throw new IllegalMethodCallException(METHOD_CALL_EXCEPTION_MSG);
        }
        String[] vars = m.group(2).split(",");
        if (Method.isNoVars(vars)) {
            vars = new String[]{};
        }
        if (!checkMethodCallVars(methodCalled, vars)) {
            throw new IllegalMethodCallException(ILLEGAL_METHOD_CALL_EXCEPTION_MSG);
        }
    }

    private boolean checkMethodCallVars(Method methodCalled, String[] vars) {
        List<VarType> paramsList = methodCalled.getParamList();
        if (vars.length != paramsList.size()) {
            return false;
        }
        for (int i = 0; i < vars.length; i++) {
            VarType paramType = paramsList.get(i);
            String var = vars[i].strip();

            // Check if var value match type
            if (!VarTypeFactory.getValValidationFunc(paramType).apply(var)) {
                // Check if value is a variable in this method scope
                Variable varInScope = currentScope.isVarInScope(var);
                if (varInScope == null) {
                    return false;
                }
                if (!checkVarInMethodCall(varInScope, paramType)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkVarInMethodCall(Variable var, VarType type) {
        return VarTypeFactory.assignTypesMatch(type, var.getType()) && var.isInitialized();
    }
}
