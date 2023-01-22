package oop.ex6.componants.methods;

import oop.ex6.componants.VarType;
import oop.ex6.componants.variables.InvalidVarTypeException;
import oop.ex6.componants.variables.VarTypeFactory;
import oop.ex6.componants.variables.Variable;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scope {
    private HashMap<String, Variable> variableHashMap;
    private Scope parentScope;
    private static final String[] LEGAL_SCOPE_NAMES = {"if", "while"};
    private static final String SCOPE_NAMES_REGEX = "(" + String.join("|", LEGAL_SCOPE_NAMES) + ")";
    private static final String PARENTHESES_REGEX = "\\((.+)\\)";
    private static final String SCOPE_REGEX = "\\s*" + SCOPE_NAMES_REGEX + "\\s*" + PARENTHESES_REGEX + "\\s*";
    private static final String[] LEGAL_AND_OR_REGEX = {"\\&\\&", "\\|\\|"};
    private static final String AND_OR_REGEX = "(" + String.join("|", LEGAL_AND_OR_REGEX) + ")";
    private static final String NO_SPACE_REGEX = "\\S+";
    private static final String CONDITION_REGEX ="\\s*(\\S+)[\\s*\\#\\s*(\\S+)\\s*]*";
//    private static final String CONDITION_REGEX ="\\s*([0-9a-zA-Z_\\-\\+\\.]+)[\\s*\\#\\s*" +
//            "([0-9a-zA-Z_\\-\\+\\.]+)\\s*]*";
    private static final String VAR_IN_CONDITION_REGEX ="([0-9a-zA-Z_]+)";

    private static final Pattern SCOPE_PATTERN = Pattern.compile(SCOPE_REGEX);
    private static final Pattern AND_OR_PATTERN = Pattern.compile(AND_OR_REGEX);
    private static final Pattern VAR_IN_CONDITION_PATTERN = Pattern.compile(VAR_IN_CONDITION_REGEX);
    private static final Pattern CONDITION_PATTERN = Pattern.compile(CONDITION_REGEX);


    public Scope(Scope parentScope) {
        this.parentScope = parentScope;
        variableHashMap = new HashMap<>();
    }

    public boolean addVarToScopeMap(Variable var){
        if (variableHashMap.containsKey(var.getName())) {
            return false;
        }
        variableHashMap.put(var.getName(), var);
        return true;
    }

    public Variable getVarFromMap(String varName){
        if (variableHashMap.containsKey(varName)) {
            return variableHashMap.get(varName);
        }
        if (parentScope == null) {
            return null;
        }
        return parentScope.getVarFromMap(varName);
    }

    public Variable isVarInScope(String varName){
        return variableHashMap.getOrDefault(varName, null);
    }

    public static boolean isValidScopeDeclaration(String declarationLine) {
        Matcher m = SCOPE_PATTERN.matcher(declarationLine);
        return m.matches();
    }

    public static void isValidCondition(String declarationLine, Scope parentScope) throws IllegalConditionException {
        Matcher m = SCOPE_PATTERN.matcher(declarationLine);
        if (!m.matches()) {
            throw new IllegalConditionException();
        }
        String condition = m.group(2);
        condition = condition.replaceAll(AND_OR_REGEX,"#");
        m = CONDITION_PATTERN.matcher(condition);
        if(!m.matches()){
            throw new IllegalConditionException();
        }
        String[] varsInCond = condition.split("#");
        for (String varInCond: varsInCond) {
            varInCond = varInCond.strip();
            if(!isValidSingleCondition(varInCond, parentScope)){
                throw new IllegalConditionException();
            }
        }
    }

    private static boolean isValidSingleCondition(String statement, Scope parentScope) {
        // Check if statement is a valid boolean value
        if (VarTypeFactory.getValValidationFunc(VarType.BOOLEAN).apply(statement)) {
            return true;
        }

        // Check if statement is an initialized var
//        Variable varInScope = this.isVarInScope(statement);
        Variable varInParent = parentScope.isVarInScope(statement);
//        if (varInScope != null) {
//            return varInScope.isInitialized();
//        } else
        if (varInParent != null && varInParent.isInitialized()) {
            return true;
        }
        return false;
    }

    public Scope getParentScope() {
        return parentScope;
    }
}
