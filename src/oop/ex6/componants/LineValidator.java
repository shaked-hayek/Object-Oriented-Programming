package oop.ex6.componants;

import oop.ex6.componants.methods.GlobalScope;
import oop.ex6.componants.variables.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineValidator {

    private static final String VALID_END_REGEX = ".*[;|}|{]\\s*";
    private static final String VAR_LINE_END = ".*;\\s*";
    private static final String SCOPE_OPEN_LINE_END = ".*{\\s*";
    private GlobalScope globalScope;

    public LineValidator(GlobalScope globalScope) {
        this.globalScope = globalScope;
    }

    public void validate(String line)
            throws InvalidLineEndException, InvalidVarTypeException, ValueMismatchException,
            InvalidVarDeclarationException, VarNameInitializedException {
        if (!isRegexMatches(line, VALID_END_REGEX)){
            throw new InvalidLineEndException();
        }
        if (isRegexMatches(line, VAR_LINE_END)) {
            line = line.replaceAll(";", "");
            new Variables(line, globalScope);
        } else if (isRegexMatches(line, SCOPE_OPEN_LINE_END)) {
            //TODO
        }
    }

    private static boolean isRegexMatches(String line, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        return m.matches();
    }
}
