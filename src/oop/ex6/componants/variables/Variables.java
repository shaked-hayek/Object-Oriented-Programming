package oop.ex6.componants.variables;

import oop.ex6.componants.VarType;
import oop.ex6.componants.methods.Scope;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variables {
    private static final String FINAL_REGEX = "\\s*final\\s+";
    private static final String TYPE_REGEX = "\\s*([a-zA-Z]+)\\s+(.*)";
    private static boolean isFinal;
    private Scope scope;


    public Variables(String line, Scope scope)
            throws InvalidVarTypeException, InvalidVarDeclarationException, ValueTypeMismatchException, VarNameInitializedException {
        // Check final
        isFinal = isFinal(line);
        this.scope = scope;
        if (isFinal) {
            line = line.replaceAll(FINAL_REGEX, "");
        }

        // Get type
        Pattern p = Pattern.compile(TYPE_REGEX);
        Matcher m = p.matcher(line);
        if (!m.lookingAt()) {
            throw new InvalidVarTypeException();
        }
        VarType type = VarTypeFactory.getType(m.group(1));
        String restOfLine = m.group(2);

        // Get different vars
        String[] vars = restOfLine.split(",");
        if (vars.length < 1) { // No variables
            throw new InvalidVarDeclarationException();
        }
        for (String var : vars) {
            Variable variable = new Variable(type, var, scope, isFinal);
            if (!scope.addVarToScopeMap(variable)) {
                throw new VarNameInitializedException();
            }
        }
    }

    private boolean isFinal(String line) {
        Pattern p = Pattern.compile(FINAL_REGEX);
        Matcher m = p.matcher(line);
        return m.lookingAt();
    }
}
