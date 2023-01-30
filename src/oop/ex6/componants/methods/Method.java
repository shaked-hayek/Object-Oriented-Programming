package oop.ex6.componants.methods;

import oop.ex6.componants.variables.VarType;
import oop.ex6.componants.variables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Method extends Scope {
    private static final String VAR_METHOD_DECLARATION_EXCEPTION_MSG =
            "Illegal variable in method declaration";
    private static final String METHOD_DECLARATION_EXCEPTION_MSG = "Illegal method declaration";

    private static final String NAME_REGEX = "([a-zA-Z]+[a-zA-Z0-9_]*)";
    private static final String VOID_REGEX = "void";
    private static final Pattern VOID_PATTERN =  Pattern.compile("\\s*" + VOID_REGEX);

    private static final String VAR_REGEX = "\\s*" + VarTypeFactory.getTypeRegex() + "\\s+" + NAME_REGEX + "\\s*";
    private static final Pattern VAR_PATTERN =  Pattern.compile(VAR_REGEX);
    private static final String VARS_REGEX = "\\((.*)\\)";
    private static final String INIT_REGEX = "\\s*" + VOID_REGEX + "\\s*" + NAME_REGEX + "\\s*" + VARS_REGEX + "\\s*";
    private static final Pattern INIT_PATTERN =  Pattern.compile(INIT_REGEX);
    /**
     * method name
     */
    private final String name;

    /**
     * list of parameters type in the method (by their order)
     */
    private List<VarType> paramsList;

    /**
     * constructor
     * @param parentScope global scope
     * @param line of method declaration
     * @throws MethodDeclarationException when method declaration is invalid
     * @throws InvalidVarTypeException when variable type is invalid
     * @throws VariableDeclarationException when variables declaration is invalid
     * @throws VariableAssignmentException when variable assignment is invalid
     */
    public Method (Scope parentScope, String line) throws MethodDeclarationException,
            InvalidVarTypeException,
            VariableDeclarationException, VariableAssignmentException {
        super(parentScope);
        paramsList = new ArrayList<>();

        Matcher m = INIT_PATTERN.matcher(line);
        if (!m.matches()){
            throw new MethodDeclarationException(METHOD_DECLARATION_EXCEPTION_MSG);
        }
        name = m.group(1);
        String[] vars = m.group(2).split(",");
        if (!isNoVars(vars)) {
            for (String varStr : vars) {
                boolean isVarFinal = false;
                if (Variables.isFinal(varStr)) {
                    isVarFinal = true;
                    varStr = Variables.stripFinal(varStr);
                }

                m = VAR_PATTERN.matcher(varStr);
                if (m.matches()) {
                    VarType varType = VarTypeFactory.getType(m.group(1));
                    String varName = m.group(2);
                    Variable var = new Variable(
                            VarTypeFactory.getType(m.group(1)), varName, parentScope, isVarFinal, true);
                    if (!addVarToScopeMap(var)) {
                        throw new MethodDeclarationException(VAR_METHOD_DECLARATION_EXCEPTION_MSG);
                    }
                    paramsList.add(varType);
                } else {
                    throw new MethodDeclarationException(METHOD_DECLARATION_EXCEPTION_MSG);
                }
            }
        }
    }

    /**
     * checks if method's arguments list is empty
     * @param vars method's arguments list
     * @return true if a method arguments list is empty
     */
    public static boolean isNoVars(String[] vars) {
        return Objects.equals(vars[0].strip(), "") && vars.length == 1;
    }

    /**
     *
     * @return method's name
     */
    public String getName() {
        return name;
    }

    /**
     * gets a line and make sure it starts with "void" regex as method
     * @param line to check if is method
     * @return true if pattern matches the method line
     */
    public static boolean checkIsMethod(String line) {
        Matcher m = VOID_PATTERN.matcher(line);
        return m.lookingAt();
    }

    /**
     *
     * @return the list of method's argument types
     */
    public List<VarType> getParamList() {
        return paramsList;
    }
}
