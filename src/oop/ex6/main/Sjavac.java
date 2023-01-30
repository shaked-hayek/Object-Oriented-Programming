package oop.ex6.main;

import oop.ex6.components.scopes.GlobalScope;
import oop.ex6.components.scopes.IllegalConditionException;
import oop.ex6.components.scopes.MethodDeclarationException;
import oop.ex6.components.variables.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sjavac {
    private static final int VALID_ARG_NUM = 1;
    private static final String VALID_CODE = "0";
    private static final String INVALID_CODE = "1";
    private static final String IO_ERROR_CODE = "2";
    private static final String ERROR_CODE = "ERROR: ";
    private static final String LINE_CODE = " (line %d)";
    private static final String METHOD_LINE_CODE = " (in method '%s')";

    private static final String IO_ERR_MSG = "File not found";
    private static final String ARG_ERR_MSG = "Wrong number of arguments entered";
    private static final String COMMENT_REGEX = "^[/]{2}.*";
    private static final Pattern COMMENT_PATTERN = Pattern.compile(COMMENT_REGEX);
    private static final String SPACE_REGEX = "\\s*";
    private static final Pattern SPACE_PATTERN = Pattern.compile(SPACE_REGEX);

    public static void main(String[] args) {
        if (args.length != VALID_ARG_NUM) {
            System.out.println(IO_ERROR_CODE);
            System.err.println(ERROR_CODE + ARG_ERR_MSG);
            return;
        }

        GlobalScope globalScope = new GlobalScope();
        LineValidator lv = new LineValidator(globalScope);
        if (!firstPass(lv, args[0])) {
            return;
        }
        if (!secondPass(globalScope, lv)) {
            return;
        }

        System.out.println(VALID_CODE);
    }

    /**
     * checks if a line contains only whitespace
     * @param line to be checked
     * @return true if a line contains only whitespace, else false
     */
    private static boolean isEmptyLine(String line) {
        Matcher m = SPACE_PATTERN.matcher(line);
        return m.matches();
    }

    /**
     * checks if a line represents a comment line
     * @param line to be checked
     * @return true if a line represents a comment line (starts with //)
     */
    private static boolean isComment(String line) {
        Matcher m = COMMENT_PATTERN.matcher(line);
        return m.matches();
    }

    /**
     * used whenever an Exception was raised to print the most specific informative message to the user
     * @param e Exception that was raised
     * @param lineMsg specific informative message to the user
     */
    private static void printError(Exception e, String lineMsg) {
        System.out.println(INVALID_CODE);
        System.err.printf(e + lineMsg);
        System.out.println();
    }

    /**
     * goes over the code for the first time and make sure it's valid
     * @param lv object goes over the code for the first time - keeps variables and methods declarations
     * @param fileName containing the code
     * @return false if any issue was raised going over the code the first time, otherwise true
     */
    private static boolean firstPass(LineValidator lv, String fileName) {
        String line;
        int lineIndex = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                lineIndex++;
                if (isComment(line) || isEmptyLine(line)) {
                    continue;
                }
                try {
                    lv.validate(line);
                } catch (ScopeException | InvalidVarTypeException | VariableDeclarationException |
                         MethodDeclarationException | VariableAssignmentException e) {
                    printError(e, String.format(LINE_CODE, lineIndex));
                    return false;
                }
            }
            if (!finalCheck(lv, lineIndex)) {
                return false;
            }
        } catch (IOException e) {
            System.out.println(IO_ERROR_CODE);
            System.err.println(IO_ERR_MSG);
            return false;
        }
        return true;
    }

    /**
     * makes sure the code has equal number of openers and closers "{", "}"
     * @param lv object goes over the code for the first time - keeps variables and methods declarations
     * @param lineIndex of issue
     * @return false if number of openers and closers is not the same at the end of code, otherwise true
     */
    private static boolean finalCheck(LineValidator lv, int lineIndex) {
        try {
            lv.finalCheck();
        } catch (ScopeException e) {
            printError(e, String.format(LINE_CODE, lineIndex));
            return false;
        }
        return true;
    }


    /**
     * goes over the code for the second time (mostly methods content and inner if\while content),
     * and make sure it's valid
     * @param globalScope most upper scope in code, now containing all variables\method declarations
     * @param lv object goes over the code for the first time - keeps variables and methods declarations
     * @return false if any issue was raised going over the code the second time, otherwise true
     */
    private static boolean secondPass(GlobalScope globalScope, LineValidator lv) {
        for (String methodName : globalScope.getMethods()) {
            MethodValidator mv = new MethodValidator(globalScope, lv, methodName);
            try {
                mv.validate();
            } catch (IllegalMethodCallException | ReturnStatementException | IllegalConditionException |
                     InvalidVarTypeException | VariableDeclarationException | VariableAssignmentException e) {
                printError(e, String.format(METHOD_LINE_CODE, methodName));
                return false;
            }
        }
        return true;
    }
}
