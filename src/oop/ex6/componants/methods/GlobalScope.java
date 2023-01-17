package oop.ex6.componants.methods;

import oop.ex6.componants.variables.Variable;

import java.util.HashMap;

public class GlobalScope extends Scope {
    private HashMap<String, Method> methodHashMap;

    public GlobalScope() {
        super(null);
        methodHashMap = new HashMap<>();
    }

    public void addMethodToScopeMap(Method method){
        methodHashMap.put(method.getName(), method);
    }

    public Method getMethodFromMap(String methodName){
        return methodHashMap.getOrDefault(methodName, null);
    }
}
