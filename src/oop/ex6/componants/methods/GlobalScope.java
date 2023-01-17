package oop.ex6.componants.methods;

import oop.ex6.componants.variables.Variable;

import java.util.HashMap;

public class GlobalScope extends Scope {
    private HashMap<String, Method> methodHashMap;

    public GlobalScope() {
        super(null);
        methodHashMap = new HashMap<>();
    }

    public boolean addMethodToScopeMap(Method method){
        if (methodHashMap.containsKey(method.getName())) {
            return false;
        }
        methodHashMap.put(method.getName(), method);
        return true;
    }

    public Method getMethodFromMap(String methodName){
        return methodHashMap.getOrDefault(methodName, null);
    }
}
