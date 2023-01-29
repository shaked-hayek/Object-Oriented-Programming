package oop.ex6.componants.variables;

import oop.ex6.componants.methods.Scope;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {
    private Scope scope;
    private boolean isFinal;
    private String name;
    private Function<String, Boolean> isValidTypeFunc;
    private boolean methodParam;
    private boolean isInitialized = false;

    private static final String FINAL_VAR_DECLARATION_EXCEPTION_MSG =
            "Final variable declaration without initialization";
    private static final String VAR_INIT_EXCEPTION_MSG = "Illegal Variable initialization";
    private static final String VALUE_ASSIGNMENT_EXCEPTION_MSG =
            "Value assigment failed - value doesn't match type declared or not initialized";

    private static final String NAME_REGEX = "([a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)";
    private static final String SPACE_REGEX = "\\s*";
    private static final Pattern DECLARATION_PATTERN = Pattern.compile(
            SPACE_REGEX + NAME_REGEX + SPACE_REGEX);
    private static final Pattern INITIALIZATION_PATTERN = Pattern.compile(
            SPACE_REGEX + NAME_REGEX + "\\s*=\\s*(.*)");

    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);
    private VarType type;

    public Variable(VarType type, String declaration, Scope scope, boolean isFinal, boolean methodParam)
            throws InvalidVarTypeException, VariableAssignmentException, VariableDeclarationException {
        this.type = type;
        this.scope = scope;
        this.isFinal = isFinal;

        isValidTypeFunc = VarTypeFactory.getValValidationFunc(type);
        this.methodParam = methodParam;
        create(declaration);
        if (methodParam) {
            isInitialized = true;
        }
    }

    public void create(String declaration) throws VariableDeclarationException, VariableAssignmentException {
        Matcher declarationMatcher = DECLARATION_PATTERN.matcher(declaration);
        Matcher initMatcher = INITIALIZATION_PATTERN.matcher(declaration);

        if (initMatcher.matches()) {
            name = initMatcher.group(1);
            String value = initMatcher.group(2).stripTrailing();
            checkAssigment(value);
        } else if (declarationMatcher.matches()) {
            if (isFinal && !methodParam) {
                throw new VariableAssignmentException(FINAL_VAR_DECLARATION_EXCEPTION_MSG);
            }
            name = declarationMatcher.group(1);
        } else {
            throw new VariableDeclarationException(VAR_INIT_EXCEPTION_MSG);
        }
    }

    public void checkAssigment(String value) throws VariableAssignmentException {
        if (!isValidTypeFunc.apply(value)) {
            if (!checkValueInScope(value)) {
                throw new VariableAssignmentException(VALUE_ASSIGNMENT_EXCEPTION_MSG);
            }
        }
        isInitialized = true;
    }

    private boolean checkValueInScope(String value) {
        Variable var = scope.getVarFromMap(value);
        if (var != null) {
            return (var.isInitialized()) && (isVarTypeMatch(var.getType()));
        }
        return false;
    }

    public boolean isVarTypeMatch(VarType assignedType) {
        return VarTypeFactory.assignTypesMatch(type, assignedType);
    }

    public VarType getType() {
        return type;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public String getName() {
        return name;
    }

    public static boolean isValidName(String varName){
        Matcher m = NAME_PATTERN.matcher(varName);
        return m.matches();

    }
}
