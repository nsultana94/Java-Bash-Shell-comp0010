package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

//*.[^\n"'`;|]* | (".["|'\w*'|`\w`))* 

public class Call implements Command {

    private String rawCommand;
   
    
	public Call(String rawC) {
        rawCommand = rawC;
	}



	public void eval(BufferedReader input, OutputStream output) throws IOException {
        ApplicationFactory applicationFactory = new ApplicationFactory();
        ArrayList<String> args = tokenizeCommand(rawCommand, output);
        String currentDirectory = directory.getCurrentDirectory();
        String cmdsub = "";
        //String regex = "`(.*?)`";

        //command substitution
        Pattern pattern = Pattern.compile("`(.*?)`");
        Matcher matcher = pattern.matcher(rawCommand);
         if (matcher.find()){
             cmdsub = matcher.group();
            cmdsub = cmdsub.replace("`", "");
            CommandSubstitution subcmd = new CommandSubstitution(cmdsub);

        }
       

        /*
         * String quotedRegex = "'.[^\n']*'|`.[^\n`]*`|\"(`.[^\n`]`|.[^\n\"`])*\"";
         * String unquotedRegex = ".[^\"'`\n;|<>]"; String argumentRegex = "(" +
         * quotedRegex + "|" + unquotedRegex + ")+"; int inputFileArgIndex; int
         * outputFileArgIndex; int commandIndex; String command;
         */

        File inputFile = null;
        File outputFile = null;
        Boolean inputFileBool = false;
        Boolean outputFileBool = false;
        String toRun = "";
        String nextArg ="";

        // check if there are input and output redirections
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            
            if(i!= args.size() - 1)
            {
                nextArg = args.get(i + 1);
            }
            else{
                nextArg = args.get(i);
            }
            
            
            if (arg.equalsIgnoreCase("<")) {
                if (inputFileBool == true) {
                    throw new RuntimeException("jsh: more than one I/O in same direction " /* + nextArg*/);
                } else if (i == args.size() - 1) {
                    throw new RuntimeException("jsh: no I/O file specified " + args.get(i));
                } else if (!new File(nextArg).exists()) {
                    throw new RuntimeException("jsh: file does not exist" /* + nextArg*/);
                } else {
                    inputFile = new File(currentDirectory + File.separator /* + nextArg*/);
                    inputFileBool = true;
                    args.remove(arg);
                    args.remove(nextArg);
                }
            } else if (arg.equalsIgnoreCase(">")) {
                if (outputFileBool = true) {
                    throw new RuntimeException("jsh: more than one I/O in same direction " /* + nextArg*/);
                } else if (i == args.size() - 1) {
                    throw new RuntimeException("jsh: no I/O file specified " + args.get(i));
                } else if (!new File(nextArg).exists()) {
                    outputFile = new File(currentDirectory + File.separator + nextArg);
                    outputFile.createNewFile();
                    outputFileBool = true;
                } else {
                    outputFile = new File(currentDirectory + File.separator + nextArg);
                    outputFileBool = true;
                    args.remove(arg);
                    args.remove(nextArg);
                }
            }
        } 


        // input and output streams
        if (outputFileBool == true) {
            output = new FileOutputStream(outputFile);
        }
        if (inputFileBool == true) {
            input = new BufferedReader(new FileReader(inputFile));
        }

        String appName = args.get(0);
        Boolean unsafe = ((rawCommand.charAt(0) == '_'))? true: false;
        appName = (unsafe? appName.substring(1): appName);
        Application command = applicationFactory.getApplication(appName, unsafe);
        
        System.out.println(appName);
        ArrayList<String> appArgs = new ArrayList<String>(args.subList(1, args.size()));
        System.out.println(appArgs);
        command.exec(appArgs, input, new OutputStreamWriter(output));
        
    }
        

    public ArrayList<String> tokenizeCommand(String rawCommands, OutputStream output) throws IOException {
    
        glob glob_processor = new glob();
        ArrayList<String> args = glob_processor.get_tokens(rawCommand);
        return args;
    } 

    

}
