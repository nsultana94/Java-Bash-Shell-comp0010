package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.util.List;


/**
 * Main Class for the application
 * 
 * @author Saachi Pahwa
 * @author Naima Sultana
 * @author Joshua Mukherjee
 */

public class Jsh {

    private static CurrentDirectory currentDirectory = CurrentDirectory.getInstance();

    /**
     * Takes the string from the command line and runs JSH
     * 
     * @param cmdline The input from the command line
     * @param output  Standard output
     * @throws IOException
     * @throws InterruptedException
     */
    public static void eval(String cmdline, OutputStream output)
            throws IOException, InterruptedException {

        JSHParser parser = new JSHParser();
        List<String> rawCommands = parser.get_sub_commands(cmdline);
        Throwable ex;

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        for (String rawCommand : rawCommands) {
            pipe p = new pipe(rawCommand);
            try {
                p.eval(input, output);
                if((ex = ExceptionHolder.getInstance().getException()) != null){
                    throw new RuntimeException(ex.getMessage());
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e.getMessage());
                
            } finally{
                ExceptionHolder.getInstance().reset();
            }
        } 
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            if (args.length != 2) {
                System.err.println("jsh: wrong number of arguments");
                return;
            }
            if (!args[0].equals("-c")) {
                System.err.println("jsh: " + args[0] + ": unexpected argument");
            }
            try {
                eval(args[1], System.out);
            } catch (Exception e) {
                System.err.println("jsh: " + e.getMessage());
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
                        System.err.println("jsh: " + e.getMessage());
                    }
                    
                }
            } finally {
                input.close();
            }
        }
    }
}
