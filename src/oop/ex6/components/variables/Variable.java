package oop.ex6.components.variables;

import oop.ex6.components.scopes.Scope;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {
    private Scope scope;
    private boolean isFinal;
    private String name;
    private Predicate<String> isValidTypeFunc;
    private boolean methodParam;
    private boolean isInitialized = false;

    private static final String FINAL_VAR_DECLARATION_EXCEPTION_MSG =
            "Final variable declaration without initialization";
    private static final String VAR_INIT_EXCEPTION_MSG = "Illegal Variable initialization";
    private static final String VALUE_ASSIGNMENT_EXCEPTION_MSG =
            "Value assigment failed - value doesn't match type declared or not initialized";

    private static final String NAME_REGEX = "([a-zA-Z][a-zA-Z0-9_]*|_[a-zA-Z0-9_]+)";
    private static final String SPACE_REGEX = "\\s*";
    private static final Pattern DECLARATION_PATTERN = Pattern.compile(
            SPACE_REGEX + NAME_REGEX + SPACE_REGEX);
    private static final Pattern INITIALIZATION_PATTERN = Pattern.compile(
            SPACE_REGEX + NAME_REGEX + "\\s*=\\s*(.*)");

    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);
    private VarType type;

    /**
     * variable object constructor
     * @param type of variable
     * @param declaration line of variable
     * @param scope current scope
     * @param isFinal true if variable is set as final
     * @param methodParam true if param line contains objects of variable
     * @throws VariableAssignmentException when variable assignment illegal or has unknown value
     * @throws VariableDeclarationException when the variable declaration is illegal
     */
    public Variable(VarType type, String declaration, Scope scope, boolean isFinal, boolean methodParam)
            throws VariableAssignmentException, VariableDeclarationException {
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

    /**
     * creates a variable and fills its filed properly
     * @param declaration the line representing a variable declaration
     * @throws VariableDeclarationException when the variable declaration is illegal
     * @throws VariableAssignmentException when variable assignment illegal or has unknown value
     */
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

    /**
     * checks if the value of variable fits its type and that it has a valid value, and sets isInitialized
     * accordingly
     * @param value of a variable
     * @throws VariableAssignmentException when variable assignment illegal (value\type is invalid)
     */
    public void checkAssigment(String value) throws VariableAssignmentException {
        if (!isValidTypeFunc.test(value)) {
            if (!checkValueInScope(value)) {
                throw new VariableAssignmentException(VALUE_ASSIGNMENT_EXCEPTION_MSG);
            }
        }
        isInitialized = true;
    }

    /**
     * checks if a variable exists in the current scope
     * @param value
     * @return
     */
    private boolean checkValueInScope(String value) {
        Variable var = scope.getVarFromMap(value);
        if (var != null) {
            return (var.isInitialized()) && (isVarTypeMatch(var.getType()));
        }
        return false;
    }

    /**
     * checks if the assigned type matches the variable type using the factory
     * @param assignedType to check if matches the declared type
     * @return true if assignedType matches the declared type, otherwise false
     */
    public boolean isVarTypeMatch(VarType assignedType) {
        return VarTypeFactory.assignTypesMatch(type, assignedType);
    }

    /**
     * get the type of variable
     * @return of variable
     */
    public VarType getType() {
        return type;
    }

    /**
     * checks if a variable is final
     * @return true if variable is final, else false
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * checks if a variable is initialized
     * @return true if variable is initialized, else false
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * get the name of variable
     * @return variable's name
     */
    public String getName() {
        return name;
    }

    /**
     * checks if varName is valid
     * @param varName String representing the variable's name
     * @return true if varName matches the name pattern value
     */
    public static boolean isValidName(String varName){
        Matcher m = NAME_PATTERN.matcher(varName);
        return m.matches();

    }
}
