package oop.ex6.componants.variables;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variables {
    private static final String FINAL_REGEX = "\\s*final\\s+";
    private static final String TYPE_REGEX = "\\s*([[a-z][A-Z]]+)\\s+(.*)";
    private static boolean isFinal;


    public Variables(String line)
            throws InvalidVarTypeException, InvalidVarDeclarationException, ValueTypeMismatchException {
        line = line.replaceAll(";", "");
        // Check final
        isFinal = isFinal(line);
        if (isFinal) {
            line = line.replaceAll(FINAL_REGEX, "");
        }

        // Get type
        Pattern p = Pattern.compile(TYPE_REGEX);
        Matcher m = p.matcher(line);
        if (!m.lookingAt()) {
            throw new InvalidVarTypeException();
        }
        Function<String, Boolean> isValidTypeFunc = VarTypeFactory.getValValidationFunc(m.group(1));
        String restOfLine = m.group(2);

        // Get different vars
        String[] vars = restOfLine.split(",");
        if (vars.length < 1) { // No variables
            throw new InvalidVarDeclarationException();
        }
        for (String var : vars) {
            new Variable(isValidTypeFunc, var, isFinal); // TODO
        }
    }

    private boolean isFinal(String line) {
        Pattern p = Pattern.compile(FINAL_REGEX);
        Matcher m = p.matcher(line);
        return m.lookingAt();
    }
}
