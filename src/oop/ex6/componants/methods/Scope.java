package oop.ex6.componants.methods;

import oop.ex6.componants.variables.Variable;

import java.util.HashMap;

public class Scope {
    private HashMap<String, Variable> variableHashMap;

    Scope(){
        variableHashMap = new HashMap<>();
    }

    void addVarToScopeMap(Variable var){
        variableHashMap.put(var.getName(), var);
    }

    Variable getVarFromMap(String varName){
        return variableHashMap.getOrDefault(varName, null);
    }





}
