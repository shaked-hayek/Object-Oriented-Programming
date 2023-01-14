package oop.ex6.componants;

import oop.ex6.componants.variables.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineValidator {

    private static final String VALID_END_REGEX = ".*[;|}|{]\\s*";
    private static final String VAR_LINE_END = ".*;\\s*";

    public void validate(String line)
            throws InvalidLineEndException, InvalidVarTypeException, ValueTypeMismatchException, InvalidVarDeclarationException {
        if (!isEndValid(line)){
            throw new InvalidLineEndException();
        }
        if (isVarEnd(line)) {
            new Variables(line);
        }
    }

    private static boolean isEndValid(String line){
        Pattern p = Pattern.compile(VALID_END_REGEX);
        Matcher m = p.matcher(line);
        return m.matches();
    }

    private static boolean isVarEnd(String line){
        Pattern p = Pattern.compile(VAR_LINE_END);
        Matcher m = p.matcher(line);
        return m.matches();
    }
}
