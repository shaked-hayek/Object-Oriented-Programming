package oop.ex6.componants.variables;

import oop.ex6.componants.methods.Scope;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for handling a line of few variables.
 */
public class Variables {
    private static final String FINAL_VAR_ASSIGMENT_EXCEPTION_MSG =
            "Illegal assigment to a final variable";
    private static final String VAR_NAME_EXCEPTION_MSG = "Variable initialization with illegal name";
    private static final String VAR_ASSIGNMENT_EXCEPTION_MSG = "Variable assignment is illegal";
    private static final String VAR_DOESNT_EXIST_EXCEPTION_MSG = "Variable assignment to unknown variable";
    private static final String NO_VARS_EXCEPTION_MSG = "No variables in declaration";
    private static final String TOO_MANY_COMMAS_EXCEPTION_MSG = "Too many commas in declaration";
    private static final String VAR_DECLARATION_EXCEPTION_MSG = "Illegal variable declaration";

    private static final String NAME_REGEX = "([a-zA-Z]+[a-zA-Z0-9_]*|_+[a-zA-Z0-9_]+)";
    private static final String FINAL_REGEX = "\\s*final\\s+";
    private static final Pattern FINAL_PATTERN = Pattern.compile(FINAL_REGEX);
    private static final Pattern TYPE_PATTERN = Pattern.compile("\\s*([a-zA-Z]+)\\s+(.*)");
    private static final Pattern ASSIGMENT_PATTERN = Pattern.compile(
            "\\s*" + NAME_REGEX + "\\s*=\\s*(\\S*)\\s*");
    private boolean isFinal;
    private Scope scope;


    /**
     * Constructor.
     * @param scope the scope the variables are in.
     */
    public Variables(Scope scope) {
        this.scope = scope;
    }

    /**
     * Process a line of initialization or assigment to variables and add them to scope.
     * @param line the line from file without ;.
     * @throws InvalidVarTypeException              The var type doesn't exist.
     * @throws VariableDeclarationException       The var declaration doesn't match the rules.
     * @throws VariableAssignmentException          Illegal assigment to a final variable.
     */
    public void processVarsLine(String line) throws InvalidVarTypeException, VariableDeclarationException,
            VariableAssignmentException {
        // Check final
        isFinal = isFinal(line);
        if (isFinal) {
            line = stripFinal(line);
        }

        if (isInitialization(line)) {
            varInitialization(line);
        } else {  // Variable assigment
            varAssigment(line);
        }
    }

    /**
     * Checks if final word in line.
     * @param line  line as string.
     * @return True if final in line, false otherwise.
     */
    public static boolean isFinal(String line) {
        Matcher m = FINAL_PATTERN.matcher(line);
        return m.lookingAt();
    }

    /**
     * Strip the word final from the line.
     * @param line line as string.
     * @return The line without the final word.
     */
    public static String stripFinal(String line) {
        return line.replaceAll(FINAL_REGEX, "");
    }

    /**
     * Checks if line is as initialization line by checking if the first word is a var type.
     * @param line  The line as string.
     * @return  True if initialization line, false otherwise.
     */
    private boolean isInitialization(String line) {
        Pattern p = Pattern.compile("\\s*" + VarTypeFactory.getTypeRegex());
        Matcher m = p.matcher(line);
        return m.lookingAt();
    }

    /**
     * Get an initialization line of shape "<type> <var>(=<value>)" check it validity and save vars to scope.
     * @param initLine initialization line
     * @throws InvalidVarTypeException          The var type doesn't exist.
     * @throws VariableDeclarationException   The var declaration doesn't match the rules.
     */
    private void varInitialization(String initLine)
            throws InvalidVarTypeException, VariableDeclarationException,
            VariableAssignmentException {
        // Get type
        Matcher m = TYPE_PATTERN.matcher(initLine);
        if (!m.lookingAt()) {
            throw new VariableDeclarationException(VAR_DECLARATION_EXCEPTION_MSG);
        }
        VarType type = VarTypeFactory.getType(m.group(1));
        String restOfLine = m.group(2);

        // Get different vars
        String[] vars = splitVarsLine(restOfLine);
        for (String var : vars) {
            Variable variable = new Variable(type, var, scope, isFinal, false);
            if (!scope.addVarToScopeMap(variable)) {
                throw new VariableDeclarationException(VAR_NAME_EXCEPTION_MSG);
            }
        }
    }

    /**
     * Get an assigment line of shape "<var>(=<value>)" check it validity and update initialization flag.
     * @param assignLine    assigment line.
     * @throws VariableDeclarationException     The var declaration doesn't match the rules.
     * @throws VariableAssignmentException      Illegal assigment to a final variable.
     */
    private void varAssigment(String assignLine)
            throws VariableDeclarationException, VariableAssignmentException {
        String[] vars = splitVarsLine(assignLine);
        for (String varStr : vars) {
            Matcher m = ASSIGMENT_PATTERN.matcher(varStr);
            if (!m.matches()) {
                throw new VariableAssignmentException(VAR_ASSIGNMENT_EXCEPTION_MSG);
            }
            String name = m.group(1);
            String value = m.group(2);
            Variable var = scope.getVarFromMap(name);
            if (var == null) {
                throw new VariableAssignmentException(VAR_DOESNT_EXIST_EXCEPTION_MSG);
            }
            if (var.isFinal()) {
                throw new VariableAssignmentException(FINAL_VAR_ASSIGMENT_EXCEPTION_MSG);
            }
            var.checkAssigment(value);
        }
    }

    /**
     * Split vars line by ",".
     * @param line  line of vars
     * @return  a list of var strings
     * @throws VariableDeclarationException   Thrown when no vars in string or too many commas
     */
    private String[] splitVarsLine(String line) throws VariableDeclarationException {
        String[] vars = line.split(",");
        if (vars.length < 1) { // No variables
            throw new VariableDeclarationException(NO_VARS_EXCEPTION_MSG);
        }
        int numberOfVars = line.length() - line.replace(",", "").length() + 1;
        if (numberOfVars != vars.length) {
            throw new VariableDeclarationException(TOO_MANY_COMMAS_EXCEPTION_MSG);
        }
        return vars;
    }
}
