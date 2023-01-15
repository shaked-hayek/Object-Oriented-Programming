package oop.ex6.componants.methods;

import oop.ex6.componants.VarType;
import oop.ex6.componants.variables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Method extends Scope {
    private static final String NAME_REGEX = "([a-zA-Z]+[a-zA-Z0-9_]*)";
    private static final String VOID_REGEX = "void";

    private static final String VAR_REGEX = "(" + VarTypeFactory.getTypeRegex() + ")" + "\\s+" + NAME_REGEX;
    private static final String VARS_REGEX = "\\((.*)\\)";
    private static final String INIT_REGEX = "\\s*" + VOID_REGEX + "\\s*" + NAME_REGEX + "\\s*" + VARS_REGEX;

    private List<VarType> paramsList;

    Method (String line) throws MethodDeclarationException, InvalidVarTypeException,
            ValueTypeMismatchException, InvalidVarDeclarationException {
        super();
        paramsList = new ArrayList<>();

        Pattern p = Pattern.compile(INIT_REGEX);
        Matcher m = p.matcher(line);
        if (!m.matches()){
            throw new MethodDeclarationException();
        }
        String[] vars = m.group(1).split(",");
        for (String varStr: vars){
            p = Pattern.compile(VAR_REGEX);
            m = p.matcher(varStr);
            if (m.matches()){
                VarType varType = VarTypeFactory.getType(m.group(1));
                String varName = m.group(2);
                Variable var = new Variable(
                        VarTypeFactory.getValValidationFunc(m.group(1)), varName, false);
                addVarToScopeMap(var);
                paramsList.add(varType);
            } else {
                throw new MethodDeclarationException();
            }
        }

    }


}
