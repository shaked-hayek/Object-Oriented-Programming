package oop.ex6.componants.variables;

import oop.ex6.componants.VarType;
import oop.ex6.componants.methods.Scope;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {
    private Scope scope;
    private boolean isFinal;
    private String name;
    private Function<String, Boolean> isValidTypeFunc;
    private boolean isInitialized = false;

    private static final String NAME_REGEX = "([a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)";
    private static final String SPACE_REGEX = "\\s*";
    private static final Pattern DECLARATION_PATTERN = Pattern.compile(
            SPACE_REGEX + NAME_REGEX + SPACE_REGEX);
    private static final Pattern INITIALIZATION_PATTERN = Pattern.compile(
            SPACE_REGEX + NAME_REGEX + "\\s*=\\s*(.*)");

    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);
    private VarType type;

    public Variable(VarType type, String declaration, Scope scope, boolean isFinal)
            throws ValueMismatchException, VarNameInitializedException, InvalidVarTypeException,
            IllegalFinalVarAssigmentException {
        this.type = type;
        this.scope = scope;
        this.isFinal = isFinal;

        isValidTypeFunc = VarTypeFactory.getValValidationFunc(type);
        if (isValidTypeFunc == null) {
            throw new InvalidVarTypeException();
        }
        create(declaration);
    }

    public void create(String declaration)
            throws ValueMismatchException, VarNameInitializedException, IllegalFinalVarAssigmentException {
        Matcher declarationMatcher = DECLARATION_PATTERN.matcher(declaration);
        Matcher initMatcher = INITIALIZATION_PATTERN.matcher(declaration);

        if (initMatcher.matches()) {
            name = initMatcher.group(1);
            String value = initMatcher.group(2).stripTrailing();
            checkAssigment(value);
        } else if (declarationMatcher.matches()) {
            if (isFinal) {
                throw new IllegalFinalVarAssigmentException();
            }
            name = declarationMatcher.group(1);
        } else {
            throw new VarNameInitializedException();
        }
    }

    public boolean checkAssigment(String value) throws ValueMismatchException {
        if (!isValidTypeFunc.apply(value)) {
            if (!checkValueInScope(value)) {
                throw new ValueMismatchException();
            }
        }
        isInitialized = true;
        return true;
    }

    private boolean checkValueInScope(String value) {
        Variable var = scope.getVarFromMap(value);
        if (var != null) {
            return (var.isInitialized()) && (isVarTypeMatch(var.getType()));
        }
        return false;
    }

    public boolean isVarTypeMatch(VarType assignedType) {
        return isTypesMatch(type, assignedType);
    }

    public static boolean isTypesMatch(VarType varType, VarType assignedType) {
        if (varType == assignedType) {
            return true;
        } else if (varType == VarType.DOUBLE && assignedType == VarType.INT) {
            return true;
        } else if (varType == VarType.BOOLEAN &&
                (assignedType == VarType.INT || assignedType == VarType.DOUBLE)) {
            return true;
        }
        return false;
    }

    public VarType getType() {
        return type;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setInitializedTrue() {
        isInitialized = true;
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
