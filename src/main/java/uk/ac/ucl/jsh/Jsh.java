package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.util.ArrayList;

/**
 * Main Class for the application
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class Jsh{

    private static CurrentDirectory currentDirectory = CurrentDirectory.getInstance();


    /**
     * Takes the string from the command line and runs JSH
     * 
     * @param cmdline The input from the command line
     * @param output  Standard output
     * @throws IOException
     * @throws InterruptedException
     */
    public static void eval(String cmdline, OutputStream output) throws IOException, InterruptedException {


        JSHParser parser = new JSHParser();
        ArrayList<String> rawCommands = parser.get_sub_commands(cmdline);

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        for (String rawCommand : rawCommands){
            pipe p = new pipe(rawCommand);
            p.eval(input, output);
        } 
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("jsh: wrong number of arguments");
                return;
            }
            if (!args[0].equals("-c")) {
                System.out.println("jsh: " + args[0] + ": unexpected argument");
            }
            try {
                eval(args[1], System.out);
            } catch (Exception e) {
                System.out.println("jsh: " + e.getMessage());
            }
        } else {
            System.out.println("Welcome to JSH!");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            
            try {
                while (true) {
                    String prompt = currentDirectory.getCurrentDirectory() + "> ";
                    System.out.print(prompt);
                    try {
                        String cmdline = input.readLine();
                        eval(cmdline, System.out);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        System.out.println("jsh: " + e.getMessage() + e.getClass());
                    }
                }
            } finally {
                input.close();
            }
        }
    }
}
