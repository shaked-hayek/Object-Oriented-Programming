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
    private static final String DECLARATION_REGEX = "\\s*" + NAME_REGEX + "\\s*";
    private static final String INITIALIZATION_REGEX = "\\s*" + NAME_REGEX + "\\s*=\\s*(\\S+)\\s*";
    private VarType type;

    public Variable(VarType type, String declaration, Scope scope, boolean isFinal)
            throws ValueMismatchException, VarNameInitializedException, InvalidVarTypeException {
        this.type = type;
        this.scope = scope;
        this.isFinal = isFinal;

        isValidTypeFunc = VarTypeFactory.getValValidationFunc(type);
        create(declaration);
    }

    public void create(String declaration) throws ValueMismatchException, VarNameInitializedException {
        Pattern declarationPattern = Pattern.compile(DECLARATION_REGEX);
        Pattern initPattern = Pattern.compile(INITIALIZATION_REGEX);
        Matcher declarationMatcher = declarationPattern.matcher(declaration);
        Matcher initMatcher = initPattern.matcher(declaration);

        if (initMatcher.matches()) {
            name = initMatcher.group(1);
            String value = initMatcher.group(2);
            checkAssigment(value);
        } else if (declarationMatcher.matches()) {
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
            return (var.isInitialized()) && (var.getType() == type);
        }
        return false;
    }

    private VarType getType() {
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
}
