package oop.ex6.componants.variables;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {
    private boolean isFinal;
    private String name;
    private Function<String, Boolean> isValidTypeFunc;
    private boolean isInitialized = false;

    private static final String NAME_REGEX = "([a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)";
    private static final String DECLARATION_REGEX = "\\s*" + NAME_REGEX + "\\s*";
    private static final String INITIALIZATION_REGEX = "\\s*" + NAME_REGEX + "\\s*=\\s*(\\S+)\\s*";

    public Variable(Function<String, Boolean> isValidTypeFunc, String declaration, boolean isFinal)
            throws ValueTypeMismatchException, InvalidVarDeclarationException {
        this.isValidTypeFunc = isValidTypeFunc;
        this.isFinal = isFinal;

        create(declaration);
    }

    public void create(String declaration) throws ValueTypeMismatchException, InvalidVarDeclarationException {
        Pattern declarationPattern = Pattern.compile(DECLARATION_REGEX);
        Pattern initPattern = Pattern.compile(INITIALIZATION_REGEX);
        Matcher declarationMatcher = declarationPattern.matcher(declaration);
        Matcher initMatcher = initPattern.matcher(declaration);

        if (initMatcher.matches()) {
            name = initMatcher.group(1);
            if (!isValidTypeFunc.apply(initMatcher.group(2))) {
                throw new ValueTypeMismatchException();
            }
            isInitialized = true;
        } else if (declarationMatcher.matches()) {
            name = declarationMatcher.group(1);
        } else {
            throw new InvalidVarDeclarationException();
        }
    }


    public boolean isFinal() {
        return isFinal;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public String getName() {
        return name;
    }
}
