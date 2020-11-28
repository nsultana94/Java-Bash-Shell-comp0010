package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;


public class Jsh {

    private static CurrentDirectory currentDirectory = CurrentDirectory.getInstance();

    public static void eval(String cmdline, OutputStream output) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);

        CharStream parserInput = CharStreams.fromString(cmdline);
        JshGrammarLexer lexer = new JshGrammarLexer(parserInput);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JshGrammarParser parser = new JshGrammarParser(tokenStream);
        ParseTree tree = parser.command(); 
        ArrayList<String> rawCommands = new ArrayList<String>();
        String lastSubcommand = "";
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (!tree.getChild(i).getText().equals(";")) {
                lastSubcommand += tree.getChild(i).getText();
            } else {
                rawCommands.add(lastSubcommand);
                lastSubcommand = "";
            }
        }
        rawCommands.add(lastSubcommand);

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        for (String rawCommand : rawCommands){
            Call call = new Call(rawCommand);
            call.eval(input, output);
        } 
        
        
        // would iterate through raw commands and check if it is pipe or a normal call
        /*ApplicationFactory applicationFactory = new ApplicationFactory();
        glob glob_processor = new glob();
        for (String rawCommand : rawCommands) {
            List<String> tokens = glob_processor.get_tokens(rawCommand);
            String appName = tokens.get(0);
            Boolean unsafe = ((rawCommand.charAt(0) == '_'))? true: false;
            appName = (unsafe? appName.substring(1): appName);
            ArrayList<String> appArgs = new ArrayList<String>(tokens.subList(1, tokens.size()));
           
            Application command = applicationFactory.getApplication(appName, unsafe);
            
             */
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
                        System.out.println("jsh: " + e.getMessage());
                    }
                }
            } finally {
                input.close();
            }
        }
    }
}
