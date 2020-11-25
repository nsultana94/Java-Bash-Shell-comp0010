package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.io.OutputStreamWriter;

//*.[^\n"'`;|]* | (".["|'\w*'|`\w`))* 

public class Call implements Command {

    private Application app;
    private ArrayList<String> args;

    public Call(String type, ArrayList<String> Args, Boolean unsafe) throws IOException {
        app = new ApplicationFactory().getApplication(type, unsafe);
        args = Args;

        String quotedRegex = "'.[^\n']*'|`.[^\n`]*`|\"(`.[^\n`]`|.[^\n\"`])*\"";
        String unquotedRegex = ".[^\"'`\n;|<>]";
        String argumentRegex = "(" + quotedRegex + "|" + unquotedRegex + ")+";

        Integer argumentArgsCount = 0;
        Integer redirectionBeforeArgsCount = 0;
        Integer AfterCommandArgsCount = 0;

        for (String arg : args) {
            if (arg.matches(argumentRegex)) {
                argumentArgsCount += 1;
            } else if (argumentArgsCount == 0 && !arg.matches(argumentRegex)) {
                redirectionBeforeArgsCount += 1;
            } else if (argumentArgsCount != 0 && !arg.matches(argumentRegex)){
                AfterCommandArgsCount += 1;
            }
        }

        //REDIRECTIONS BEFORE main command!
        ArrayList<String> inputFiles = new ArrayList<>();
        ArrayList<String> outputFiles = new ArrayList<>();
        Integer redirectionPairsBefore = (redirectionBeforeArgsCount - 1) / 2;

        //if odd number of redirection arguments, throw exception
        //because redirection must always have an argument afterwards - should come in pairs
        if (redirectionPairsBefore%2 != 0){
            throw new RuntimeException("find: wrong arguments " + args.get(0));
        }

        for (int i = 0; i <= redirectionPairsBefore;) {
            if (args.get(2 * i) == "<") {
                inputFiles.add(args.get(2 + i + 1));

            } else if (args.get(2 * i) == ">") {
                outputFiles.add(args.get(2 + i + 1));
            }
        }

        ArrayList<Integer> redirectionArgIndexes = new ArrayList<>();
        //REDIRECTIONS AFTER main command!
        for (int i = 0; i <= AfterCommandArgsCount;) {
            Integer j = argumentArgsCount + redirectionBeforeArgsCount+ i;
            if (args.get(j) == "<"){
                inputFiles.add(args.get(j+1));
                redirectionArgIndexes.add(j, j+1);
            }
            else if (args.get(j) == ">") {
                outputFiles.add(args.get(j + 1));
                redirectionArgIndexes.add(j j+1);
            }
            else if (!redirectionArgIndexes.contains(j)){
                //deal with the rest of the arguments? 
            }

        }
        



    }

    public void eval(BufferedReader input, OutputStream output) throws IOException {
        app.exec(args, input, new OutputStreamWriter(output));

    }

}