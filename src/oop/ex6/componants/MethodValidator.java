package oop.ex6.componants;

import oop.ex6.componants.methods.Method;
import oop.ex6.componants.methods.Scope;

import java.util.List;

public class MethodValidator {

    public MethodValidator(Scope globalScope, LineValidator lv, String methodName) {
        List<String> methodLines = lv.getMethodLines(methodName);
    }

    public void validate() {

    }
}
