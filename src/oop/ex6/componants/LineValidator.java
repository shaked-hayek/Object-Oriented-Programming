package oop.ex6.componants;

import oop.ex6.componants.variables.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineValidator {

    private static final String VALID_END_REGEX = ".*[;|}|{]\\s*";
    private static final String VAR_LINE_END = ".*;\\s*";
    private static final String SCOPE_OPEN_LINE_END = ".*{\\s*";

    public void validate(String line)
            throws InvalidLineEndException, InvalidVarTypeException, ValueTypeMismatchException, InvalidVarDeclarationException {
        if (!isRegexMatches(line, VALID_END_REGEX)){
            throw new InvalidLineEndException();
        }
        if (isRegexMatches(line, VAR_LINE_END)) {
            line = line.replaceAll(";", "");
            new Variables(line);
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
