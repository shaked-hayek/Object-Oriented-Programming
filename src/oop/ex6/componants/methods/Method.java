package oop.ex6.componants.methods;

import oop.ex6.componants.VarType;
import oop.ex6.componants.variables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Method extends Scope {
    private static final String NAME_REGEX = "([a-zA-Z]+[a-zA-Z0-9_]*)";
    private static final String VOID_REGEX = "void";
    private static final Pattern VOID_PATTERN =  Pattern.compile("\\s*" + VOID_REGEX);

    private static final String VAR_REGEX = "\\s*" + VarTypeFactory.getTypeRegex() + "\\s+" + NAME_REGEX + "\\s*";
    private static final Pattern VAR_PATTERN =  Pattern.compile(VAR_REGEX);
    private static final String VARS_REGEX = "\\((.*)\\)";
    private static final String INIT_REGEX = "\\s*" + VOID_REGEX + "\\s*" + NAME_REGEX + "\\s*" + VARS_REGEX + "\\s*";
    private static final Pattern INIT_PATTERN =  Pattern.compile(INIT_REGEX);
    private final String name;

    private List<VarType> paramsList;

    public Method (Scope parentScope, String line) throws MethodDeclarationException, InvalidVarTypeException,
            ValueMismatchException, VarNameInitializedException {
        super(parentScope);
        paramsList = new ArrayList<>();

        Matcher m = INIT_PATTERN.matcher(line);
        if (!m.matches()){
            throw new MethodDeclarationException();
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
                            VarTypeFactory.getType(m.group(1)), varName, parentScope, isVarFinal);
                    var.setInitializedTrue();
                    if (!addVarToScopeMap(var)) {
                        throw new MethodDeclarationException();
                    }
                    paramsList.add(varType);
                } else {
                    throw new MethodDeclarationException();
                }
            }
        }
    }

    public static boolean isNoVars(String[] vars) {
        return Objects.equals(vars[0].strip(), "") && vars.length == 1;
    }

    public String getName() {
        return name;
    }

    public static boolean checkIsMethod(String line) {
        Matcher m = VOID_PATTERN.matcher(line);
        return m.lookingAt();
    }

    public List<VarType> getParamList() {
        return paramsList;
    }
}
