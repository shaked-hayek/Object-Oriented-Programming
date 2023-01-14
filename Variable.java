package oop.ex6.componants.variables;

import oop.ex6.componants.VarType;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {
    private boolean isFinal;
    private String name;
    private Function<String, Boolean> isValidTypeFunc;
    private boolean isInitialized;

    private static final String NAME_REGEX = "([[a-z][A-Z]_]+[[a-z][A-Z][0-9]_]*)";
    private static final String DECLARATION_REGEX = "\\s*" + NAME_REGEX + "\\s*";
    private static final String INITIALIZATION_REGEX = NAME_REGEX + "\\s*=\\s*([^\\s]+)\\s*";

    public Variable(Function<String, Boolean> isValidTypeFunc, String declaration, boolean isFinal) {
        this.isValidTypeFunc = isValidTypeFunc;
        this.isFinal = isFinal;
        Pattern declarationPattern = Pattern.compile(DECLARATION_REGEX);
        Pattern initPattern = Pattern.compile(INITIALIZATION_REGEX);

        Matcher declarationMatcher = declarationPattern.matcher(declaration);
        Matcher initMatcher = initPattern.matcher(declaration);

        String varValue;

        if (initMatcher.matches()) {
            name = initMatcher.group(1);
//            if (!isValidValue(type, initMatcher.group(2))) {

//            }
            isInitialized = true;
        } else if (declarationMatcher.matches()) {
            name = initMatcher.group(1);
        } else {
//            throw invalid
        }
    }
//        this.name = ;
//    }

//    public boolean isValidValue(VarType type, String value) {
//        if (type == VarType.INT) {
//            value
//        }
//        return true;
//    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isInitialized() {
        return isInitialized;
    }
}
