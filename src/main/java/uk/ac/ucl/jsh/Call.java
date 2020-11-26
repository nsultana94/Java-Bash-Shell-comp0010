package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

//*.[^\n"'`;|]* | (".["|'\w*'|`\w`))* 

public class Call implements Command {

    private Application app;
    private ArrayList<String> args;

    public Call(String type, ArrayList<String> Args, Boolean unsafe) throws IOException {
        app = new ApplicationFactory().getApplication(type, unsafe);
        args = Args;
        String currentDirectory = directory.getCurrentDirectory();

        String quotedRegex = "'.[^\n']*'|`.[^\n`]*`|\"(`.[^\n`]`|.[^\n\"`])*\"";
        String unquotedRegex = ".[^\"'`\n;|<>]";
        String argumentRegex = "(" + quotedRegex + "|" + unquotedRegex + ")+";

        // Integer argumentArgsCount = 0;
        // ArrayList<String> commandArguments = new ArrayList<>();

        File inputFile = null;
        File outputFile = null;
        Boolean inputFileBool = false;
        Boolean outputFileBool = false;
        OutputStream output;
        int inputFileArgIndex;
        int outputFileArgIndex;
        int commandIndex;
        BufferedReader input;

        String command;
        String toRun = "";

        // check if there are input and output redirections
        for (int i = 0; i <= args.size();) {
            String arg = args.get(i);
            String nextArg = args.get(i + 1);
            if (arg == "<") {
                if (inputFileBool == true) {
                    throw new RuntimeException("find: more than one I/O in same direction " + args.get(i + 1));
                } else if (i == args.size() - 1) {
                    throw new RuntimeException("find: no I/O file specified " + args.get(i));
                } else if (!new File(nextArg).exists()) {
                    throw new RuntimeException("find: file does not exist" + args.get(i + 1));
                } else {
                    inputFile = new File(currentDirectory + File.separator + nextArg);
                    inputFileArgIndex = i;
                    inputFileBool = true;
                    args.remove(arg);
                    args.remove(nextArg);
                }
            } else if (arg == ">") {
                if (outputFileBool = true) {
                    throw new RuntimeException("find: more than one I/O in same direction " + args.get(i + 1));
                } else if (i == args.size() - 1) {
                    throw new RuntimeException("find: no I/O file specified " + args.get(i));
                } else if (!new File(nextArg).exists()) {
                    outputFile = new File(currentDirectory + File.separator + nextArg);
                    outputFile.createNewFile();
                    outputFileBool = true;
                } else {
                    outputFile = new File(currentDirectory + File.separator + nextArg);
                    outputFileArgIndex = i;
                    outputFileBool = true;
                    args.remove(arg);
                    args.remove(nextArg);
                }
            }
            /*
             * else{ if (arg.matches(argumentRegex)) { argumentArgsCount += 1;
             * commandArguments.add(arg); } }
             */
        }

        // extracting command and args in case we need it
        /*
         * if (args.get(0) == "<" || args.get(0) == ">"){ if (args.get(2) == "<" ||
         * args.get(2) == ">"){ if (args.get(4).matches(argumentRegex)) { command =
         * args.get(4); commandIndex = 4; } } else{ if
         * (args.get(2).matches(argumentRegex)) { command = args.get(2); commandIndex =
         * 2; } } } else { if (args.get(0).matches(argumentRegex)) { command =
         * args.get(0); commandIndex = 0;} }
         * 
         * for (int i = commandIndex; i <= args.size();) { String arg = args.get(i);
         * String nextArg = args.get(i + 1);
         * 
         * if (arg.matches(argumentRegex) && nextArg != "<" && nextArg != ">" { //store
         * as arguments }
         * 
         * }
         */

        // input and output streams
        if (outputFileBool == true) {
            output = new FileOutputStream(outputFile);
            // OutputStreamWriter output = new OutputStreamWriter(new
            // FileOutputStream(outputFile));
        }
        if (inputFileBool == true) {
            input = new BufferedReader(new FileReader(inputFile));
            // replace arguments with input file contents
            // add buffered reader on the end of args
        }

        for (String arg : args) {
            toRun += arg;
        }

    }

    public void eval(BufferedReader input, OutputStream output) throws IOException {
        app.exec(args, input, new OutputStreamWriter(output));

    }

}