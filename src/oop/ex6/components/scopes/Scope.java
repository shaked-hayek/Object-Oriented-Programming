package oop.ex6.components.scopes;

import oop.ex6.components.variables.VarType;
import oop.ex6.components.variables.VarTypeFactory;
import oop.ex6.components.variables.Variable;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scope {
    /**
     * hashMap of all local variables in the scope (or gloval in GlobalScope)
     */
    protected HashMap<String, Variable> variableHashMap;
    /**
     * the upper scope of the current scope
     */
    private Scope parentScope;

    private static final String INVALID_SCOPE_EXCEPTION_MSG = "Illegal condition scope statement";
    private static final String CONDITION_EXCEPTION_MSG = "Illegal condition statement";

    private static final String[] LEGAL_SCOPE_NAMES = {"if", "while"};
    private static final String SCOPE_NAMES_REGEX = "(" + String.join("|", LEGAL_SCOPE_NAMES) + ")";
    private static final String PARENTHESES_REGEX = "\\((.+)\\)";
    private static final String SCOPE_REGEX = "\\s*" + SCOPE_NAMES_REGEX + "\\s*" + PARENTHESES_REGEX + "\\s*";
    private static final String[] LEGAL_AND_OR_REGEX = {"\\&\\&", "\\|\\|"};
    private static final String AND_OR_REGEX = "(" + String.join("|", LEGAL_AND_OR_REGEX) + ")";
    private static final String CONDITION_REGEX = "\\s*(\\S+)[\\s*\\#\\s*(\\S+)\\s*]*";

    private static final Pattern SCOPE_PATTERN = Pattern.compile(SCOPE_REGEX);
    private static final Pattern CONDITION_PATTERN = Pattern.compile(CONDITION_REGEX);


    /**
     * constructor
     * @param parentScope the upper scope of the current scope
     */
    public Scope(Scope parentScope) {
        this.parentScope = parentScope;
        variableHashMap = new HashMap<>();
    }

    /**
     * adds variable to the scope hashMap
     * @param var Variable to be added
     * @return true if done, otherwise false
     */
    public boolean addVarToScopeMap(Variable var){
        if (variableHashMap.containsKey(var.getName())) {
            return false;
        }
        variableHashMap.put(var.getName(), var);
        return true;
    }

    /**
     * returns variable from the hashMap by its name if exists
     * @param varName of variable, we search
     * @return variable from the hashMap if exist
     */
    public Variable getVarFromMap(String varName){
        if (variableHashMap.containsKey(varName)) {
            return variableHashMap.get(varName);
        }
        if (parentScope == null) {
            return null;
        }
        return parentScope.getVarFromMap(varName);
    }

    /**
     * checks if var in the scope or upper scope and returns it
     * @param varName to check if exists
     * @return var in the scope or upper scope
     */
    public Variable isVarInScope(String varName){
        if (variableHashMap.containsKey(varName)) {
            return variableHashMap.get(varName);
        }
        return parentScope.isVarInScope(varName);
    }

    /**
     * check if the scope declaration is valid (if\while\method ())
     * @param declarationLine the scopes declaration line
     * @return true if the scope declaration fits its pattern
     */
    public static boolean isValidScopeDeclaration(String declarationLine) {
        Matcher m = SCOPE_PATTERN.matcher(declarationLine);
        return m.matches();
    }

    /**
     * checks if the condition is valid and fits the regex
     * @param declarationLine containing the condition
     * @param parentScope in order to check if the condition exist
     * @throws IllegalConditionException when pattern doesn't match regex of conditions or single condition
     */
    public static void isValidCondition(String declarationLine, Scope parentScope)
            throws IllegalConditionException {
        Matcher m = SCOPE_PATTERN.matcher(declarationLine);
        if (!m.matches()) {
            throw new IllegalConditionException(INVALID_SCOPE_EXCEPTION_MSG);
        }
        String condition = m.group(2);
        condition = condition.replaceAll(AND_OR_REGEX,"#");
        m = CONDITION_PATTERN.matcher(condition);
        if(!m.matches()){
            throw new IllegalConditionException(CONDITION_EXCEPTION_MSG);
        }
        String[] varsInCond = condition.split("#");
        for (String varInCond: varsInCond) {
            varInCond = varInCond.strip();
            if(!isValidSingleCondition(varInCond, parentScope)){
                throw new IllegalConditionException(CONDITION_EXCEPTION_MSG);
            }
        }
    }

    /**
     * checks if a single condition in conditions is valid
     * @param statement single condition
     * @param parentScope to check if exists in upper scope
     * @return true id condition exists and has true\false value
     */
    private static boolean isValidSingleCondition(String statement, Scope parentScope) {
        // Check if statement is a valid boolean value
        if (VarTypeFactory.getValValidationFunc(VarType.BOOLEAN).apply(statement)) {
            return true;
        }

        Variable var = parentScope.isVarInScope(statement);
        if (var != null && var.isInitialized() &&
                VarTypeFactory.CONDITION_TYPE_ALLOWED.contains(var.getType())) {
            return true;
        }
        return false;
    }

    /**
     * returns parent scope
     * @return the upper scope
     */
    public Scope getParentScope() {
        return parentScope;
    }
}
