package oop.ex6.componants.variables;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VarTypeFactory {
    private static final String SPACE_REGEX = "\\s";
    private static final String INT_REGEX = "[+|-]?[0-9]+";
    private static final String DOUBLE_REGEX = "([\\+|\\-]?[0-9]+\\.?[0-9]*)|([\\+|\\-]?[0-9]*\\.?[0-9]+)";
    private static final String STRING_REGEX = "[\"][^\"]*[\"]";
    private static final String CHAR_REGEX = "['].[']";
    private static final String BOOLEAN_REGEX = "true|false|" + INT_REGEX + "|" + DOUBLE_REGEX;


    public static Function<String, Boolean> getValValidationFunc(String type) throws InvalidVarTypeException {
        switch (type) {
            case "int":
                return val -> validValueToType(INT_REGEX, val);
            case "double":
                return val -> validValueToType(DOUBLE_REGEX, val);
            case "String":
                return val -> validValueToType(STRING_REGEX, val);
            case "boolean":
                return val -> validValueToType(BOOLEAN_REGEX, val);
            case "char":
                return val -> validValueToType(CHAR_REGEX, val);
            default:
                throw new InvalidVarTypeException();
        }
    }

    private static boolean validValueToType(String regex, String val) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(val);
        return m.matches();
    }
}
