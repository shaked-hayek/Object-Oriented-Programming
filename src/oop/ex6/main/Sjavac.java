package oop.ex6.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Sjavac {
    private static final String VALID_CODE = "0";
    private static final String INVALID_CODE = "1";
    private static final String IO_ERROR_CODE = "2";
    private static final String IO_ERR_MSG = "File not found.";

    public static void main(String[] args) {
        if (args.length <= 1) {

        }
        // Load file
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {

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
}
