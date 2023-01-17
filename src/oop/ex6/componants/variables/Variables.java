package oop.ex6.componants.variables;

import oop.ex6.componants.VarType;
import oop.ex6.componants.methods.Scope;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variables {
    private static final String FINAL_REGEX = "\\s*final\\s+";
    private static final String TYPE_REGEX = "\\s*([a-zA-Z]+)\\s+(.*)";
    private static final String NAME_REGEX = "([a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)";
    private static final String ASSIGMENT_REGEX = "\\s*" + NAME_REGEX + "\\s*=\\s*(\\S*)\\s*";
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
        if (isInitialization(line)) {
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
        } else {
            Pattern p = Pattern.compile(ASSIGMENT_REGEX);
            Matcher m = p.matcher(line);
            if (!m.matches()) {
                throw new InvalidVarDeclarationException();
            }
            String name = m.group(1);
            String value = m.group(2);
            Variable var = scope.getVarFromMap(name);
            if (var == null) {
                throw new InvalidVarDeclarationException();
            }
            var.checkAssigment(value);
        }


    }

    private boolean isFinal(String line) {
        Pattern p = Pattern.compile(FINAL_REGEX);
        Matcher m = p.matcher(line);
        return m.lookingAt();
    }

    private boolean isInitialization(String line) {
        Pattern p = Pattern.compile("\\s*" + VarTypeFactory.getTypeRegex());
        Matcher m = p.matcher(line);
        return m.lookingAt();
    }
}
