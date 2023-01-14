package oop.ex6.componants.variables;

import oop.ex6.componants.VarType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variables {
    private static final String FINAL_REGEX = "\\s*final\\s+";
    private static final String TYPE_REGEX = "\\s*(int|String|char)";
    private static boolean isFinal;


    public Variables(String line) throws invalidVarTypeException, invalidVarDeclarationException {
        line = line.replaceAll(";", "");
        isFinal = isFinal(line);
        if (isFinal) {
            line = line.replace(FINAL_REGEX, "");
        }

        Pattern p = Pattern.compile(TYPE_REGEX);
        Matcher m = p.matcher(line);
        String type;
        if (m.lookingAt()) {
            type = m.group(1);
        } else { // Unknown type
            throw new invalidVarTypeException();
        }
        line = line.replace(TYPE_REGEX, "");
        String[] vars = line.split(",");
        if (vars.length < 1) { // No variables
            throw new invalidVarDeclarationException();
        }
        for (String var : vars) {
            new Variable(type, var, isFinal); // TODO
        }

    }

    private boolean isFinal(String line) {
        Pattern p = Pattern.compile(FINAL_REGEX);
        Matcher m = p.matcher(line);
        return m.lookingAt();
    }
}
