package oop.ex6.componants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineValidator {

    private static final String VALID_END_REGEX = ".*[;|}|{]\\s*";

    public void validate(String line) throws invalidLineEndException {
        if (!isEndValid(line)){
            throw new invalidLineEndException();
        }
    }

    private static boolean isEndValid(String line){
        Pattern p = Pattern.compile(VALID_END_REGEX);
        Matcher m = p.matcher(line);
        return m.matches();
    }
}
