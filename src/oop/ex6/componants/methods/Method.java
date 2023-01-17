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

    private static final String VAR_REGEX = "\\s*" + VarTypeFactory.getTypeRegex() + "\\s+" + NAME_REGEX + "\\s*";
    private static final String VARS_REGEX = "\\((.*)\\)";
    private static final String INIT_REGEX = "\\s*" + VOID_REGEX + "\\s*" + NAME_REGEX + "\\s*" + VARS_REGEX + "\\s*";
    private final String name;

    private List<VarType> paramsList;

    public Method (Scope parentScope, String line) throws MethodDeclarationException, InvalidVarTypeException,
            ValueMismatchException, InvalidVarDeclarationException, VarNameInitializedException {
        super(parentScope);
        paramsList = new ArrayList<>();

        Pattern p = Pattern.compile(INIT_REGEX);
        Matcher m = p.matcher(line);
        if (!m.matches()){
            throw new MethodDeclarationException();
        }
        name = m.group(1);
        String[] vars = m.group(2).split(",");
        if (!Objects.equals(vars[0], "")) {
            for (String varStr : vars) {
                boolean isVarFinal = false;
                if (Variables.isFinal(varStr)) {
                    isVarFinal = true;
                    varStr = Variables.stripFinal(varStr);
                }

                p = Pattern.compile(VAR_REGEX);
                m = p.matcher(varStr);
                if (m.matches()) {
                    VarType varType = VarTypeFactory.getType(m.group(1));
                    String varName = m.group(2);
                    Variable var = new Variable(
                            VarTypeFactory.getType(m.group(1)), varName, parentScope, isVarFinal);
                    var.setInitializedTrue();
                    addVarToScopeMap(var);
                    paramsList.add(varType);
                } else {
                    throw new MethodDeclarationException();
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public static boolean checkIsMethod(String line) {
        Pattern p = Pattern.compile("\\s*" + VOID_REGEX);
        Matcher m = p.matcher(line);
        return m.lookingAt();
    }
}
