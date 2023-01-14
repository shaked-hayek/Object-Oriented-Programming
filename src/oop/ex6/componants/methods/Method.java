package oop.ex6.componants.methods;

import oop.ex6.componants.variables.VarTypeFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Method extends Scope {
    private static final String NAME_REGEX = "([a-zA-Z]+[a-zA-Z0-9_]*)";
    private static final String VOID_REGEX = "void";

    private static final String VAR_REGEX = VarTypeFactory.getTypeRegex() + "\\s+" + NAME_REGEX;
    private static final String VARS_REGEX = "\\((.*)\\)";
    private static final String INIT_REGEX = "\\s*" + VOID_REGEX + "\\s*" + NAME_REGEX + "\\s*" + VARS_REGEX;

    Method(String line) throws MethodDeclarationException {

        Pattern p = Pattern.compile(INIT_REGEX);
        Matcher m = p.matcher(line);
        if (!m.matches()){
            throw new MethodDeclarationException();
        }
        String[] vars = m.group(1).split(",");
        for (String var: vars){
            p=Pattern.compile(VAR_REGEX);
            m=p.matcher(var);
            if (m.matches()){
                //TODO: keep in list of vars needed for func
            }
            else {
                throw new MethodDeclarationException();
            }
        }

    }


}
