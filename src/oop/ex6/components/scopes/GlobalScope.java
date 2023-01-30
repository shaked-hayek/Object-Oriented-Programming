package oop.ex6.components.scopes;

import oop.ex6.components.variables.Variable;

import java.util.HashMap;
import java.util.Set;

public class GlobalScope extends Scope {
    /**
     * hashMap of all methods in Global Scope
     */
    private HashMap<String, Method> methodHashMap;

    /**
     * constructor
     */
    public GlobalScope() {
        super(null);
        methodHashMap = new HashMap<>();
    }

    /**
     * adds method to scope map: method name as key and the method as value
     * @param method to be added
     * @return true upon success
     */
    public boolean addMethodToScopeMap(Method method){
        if (methodHashMap.containsKey(method.getName())) {
            return false;
        }
        methodHashMap.put(method.getName(), method);
        return true;
    }

    /**
     * get method from map by name
     * @param methodName to get the method by
     * @return method that matches the name
     */
    public Method getMethodFromMap(String methodName){
        return methodHashMap.getOrDefault(methodName, null);
    }

    /**
     * gets a set of the names of all methods in global scope
     * @return a set of the names of all methods in global scope
     */
    public Set<String> getMethods() {
        return methodHashMap.keySet();
    }

    /**
     * get the var from scope hashMap
     * @param varName to check if exists
     * @return the variable from hashMap or null if it doesn't exist
     */
    @Override
    public Variable isVarInScope(String varName) {
        return variableHashMap.getOrDefault(varName, null);
    }
}
