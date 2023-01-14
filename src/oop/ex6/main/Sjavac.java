package oop.ex6.main;

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

    private static final String IO_ERR_MSG = "File not found";
    private static final String ARG_ERR_MSG = "Wrong number of arguments entered";

    public static void main(String[] args) {
        if (args.length != VALID_ARG_NUM) {
            System.out.println(IO_ERROR_CODE);
            System.err.println(ERROR_CODE + ARG_ERR_MSG);
            return;
        }
        // Load file
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            while ((line = br.readLine()) != null) {
                if (isComment(line) || isEmptyLine(line)) {
                    continue;
                }
            }

        } catch (IOException e) {
            System.out.println(IO_ERROR_CODE);
            System.err.println(IO_ERR_MSG);
        }


//        try {
//
//        } catch () {
//            System.out.println(INVALID_CODE_MSG);
//            System.err(); // TODO
//        }
        System.out.println(VALID_CODE);

    }

    private static boolean isEmptyLine(String line) {
        Pattern p = Pattern.compile("\\s*");
        Matcher m = p.matcher(line);
        return m.matches();
    }

    private static boolean isComment(String line) {
        Pattern p = Pattern.compile("^[\\\\]{2}.*");
        Matcher m = p.matcher(line);
        return m.matches();
    }
}
