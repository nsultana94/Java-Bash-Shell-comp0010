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
    private Boolean subcmdpresent;
   
    
	public Call(String rawC) {
        rawCommand = rawC;
	}



	public void eval(BufferedReader input, OutputStream output) throws IOException {
        ApplicationFactory applicationFactory = new ApplicationFactory();
        ArrayList<String> args = tokenizeCommand(rawCommand, output);
        ArrayList<String> args1 = tokenizeCommand(rawCommand, output);

        System.out.println(args);
        String currentDirectory = directory.getCurrentDirectory();
        String cmdsub = "";
        ArrayList<String> appArgs = new ArrayList<String>();
        //String regex = "`(.*?)`";

        //command substitution
        Pattern pattern = Pattern.compile("`(.*?)`");
        Matcher matcher = pattern.matcher(rawCommand);
        if (matcher.find()){
            cmdsub = matcher.group();
            cmdsub = cmdsub.replace("`", "");
            CommandSubstitution subcmd = new CommandSubstitution(cmdsub);
            input = subcmd.get_output(input);
        }
       
        
        File inputFile = null;
        File outputFile = null;
        Boolean inputFileBool = false;
        Boolean outputFileBool = false;
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
                } else if (!new File(currentDirectory + File.separator  + nextArg).exists()) {
                    throw new RuntimeException("jsh: file does not exist "  + nextArg);
                } else {
                    inputFile = new File(currentDirectory + File.separator  + nextArg);
                    inputFileBool = true;
                    args1.remove(arg);
                    args1.remove(nextArg);
                }
            } else if (arg.equalsIgnoreCase(">")) {
                System.out.print(arg + " arg");
                if (outputFileBool = true) {
                    throw new RuntimeException("jsh: more than one I/O in same direction "  + nextArg);
                } else if (i == args.size() - 1) {
                    throw new RuntimeException("jsh: no I/O file specified " + args.get(i));
                } else if (!new File(currentDirectory + File.separator  + nextArg).exists()) {
                    outputFile = new File(currentDirectory + File.separator + nextArg);
                    outputFile.createNewFile();
                    outputFileBool = true;
                    args1.remove(arg);
                    args1.remove(nextArg);
                } else {
                    outputFile = new File(currentDirectory + File.separator + nextArg);
                    outputFileBool = true;
                    args1.remove(arg);
                    args1.remove(nextArg);
                }
            }
            if (outputFileBool == true) {
                output = new FileOutputStream(outputFile);
            }
            if (inputFileBool == true) {
                input = new BufferedReader(new FileReader(inputFile));
            }
            }
           
           


        // input and output streams
        
        

        /*
         * String quotedRegex = "'.[^\n']*'|`.[^\n`]*`|\"(`.[^\n`]`|.[^\n\"`])*\"";
         * String unquotedRegex = ".[^\"'`\n;|<>]"; String argumentRegex = "(" +
         * quotedRegex + "|" + unquotedRegex + ")+"; int inputFileArgIndex; int
         * outputFileArgIndex; int commandIndex; String command;
         */

        

        String appName = args1.get(0);
        Boolean unsafe = ((rawCommand.charAt(0) == '_'))? true: false;
        appName = (unsafe? appName.substring(1): appName);
        appArgs = new ArrayList<String>(args1.subList(1, args1.size()));
        Application command = applicationFactory.getApplication(appName, unsafe);
        
        command.exec(appArgs, input, new OutputStreamWriter(output));
        
    }
        

    public ArrayList<String> tokenizeCommand(String rawCommands, OutputStream output) throws IOException {
    
        glob glob_processor = new glob();
        ArrayList<String> args = glob_processor.get_tokens(rawCommand);
        return args;
    } 

    

}
