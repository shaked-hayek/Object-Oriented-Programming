package oop.ex6.componants.methods;

import oop.ex6.componants.variables.Variable;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scope {
    private HashMap<String, Variable> variableHashMap;
    private Scope parentScope;
    private static final String[] LEGAL_SCOPE_NAMES = {"if", "while"};
    private static final String SCOPE_NAMES_REGEX = "(" + String.join("|", LEGAL_SCOPE_NAMES) + ")";
    private static final String PARENTHESES_REGEX = "\\((.*)\\)";
    private static final String SCOPE_REGEX = "\\s*" + SCOPE_NAMES_REGEX + "\\s*" + PARENTHESES_REGEX + "\\s*";
    private static final Pattern SCOPE_PATTERN = Pattern.compile(SCOPE_REGEX);

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

    private boolean isValidCondition(String declarationLine) {
        Matcher m = SCOPE_PATTERN.matcher(declarationLine);
        if (!m.matches()) {
            return false;
        }
        String condition = m.group(2);
        return true; // TODO
    }
}
