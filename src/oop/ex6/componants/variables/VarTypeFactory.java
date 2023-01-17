package oop.ex6.componants.variables;

import oop.ex6.componants.VarType;

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
    private static final String INT = "int";
    private static final String DOUBLE = "double";
    private static final String STRING = "String";
    private static final String BOOLEAN = "boolean";
    private static final String CHAR = "char";
    private static final String[] TYPES = {INT, DOUBLE, STRING, BOOLEAN, CHAR};


    public static Function<String, Boolean> getValValidationFunc(VarType type) throws InvalidVarTypeException {
        switch (type) {
            case INT:
                return val -> validValueToType(INT_REGEX, val);
            case DOUBLE:
                return val -> validValueToType(DOUBLE_REGEX, val);
            case STRING:
                return val -> validValueToType(STRING_REGEX, val);
            case BOOLEAN:
                return val -> validValueToType(BOOLEAN_REGEX, val);
            case CHAR:
                return val -> validValueToType(CHAR_REGEX, val);
            default:
                throw new InvalidVarTypeException();
        }
    }

    public static VarType getType(String type) throws InvalidVarTypeException {
        switch (type) {
            case INT:
                return VarType.INT;
            case DOUBLE:
                return VarType.DOUBLE;
            case STRING:
                return VarType.STRING;
            case BOOLEAN:
                return VarType.BOOLEAN;
            case CHAR:
                return VarType.CHAR;
            default:
                throw new InvalidVarTypeException();
        }
    }

    private static boolean validValueToType(String regex, String val) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(val);
        return m.matches();
    }

    public static String getTypeRegex() {
        return "[" + String.join("|", TYPES) + "]";
    }
}
