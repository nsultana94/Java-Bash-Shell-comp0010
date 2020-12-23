package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Call class to represent a call to an application.
 * Handles IO redirection and String manipulation on the input
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class Call extends Thread implements Command {

    private String rawCommand;
    
    private BufferedReader input;
    private OutputStream output;
    
    public void run(){
        try {
            eval(input, output);
        } catch (Exception e) {
            if(!(e.getMessage() == null)){
                System.out.println("jsh:"+ e.getMessage());
            }
            Thread.currentThread().interrupt();
        }

    }    

    /**
     * constructor to take the raw string input from the command line
     * @param rawC the String to be processed to be executed 
     */
	public Call(String rawC) {
        rawCommand = rawC;
	}

    /**
     * Main method of the class to run the command
     * @param input {@code BufferedReader} the standard input for the command to be executed
     * @param output {@code OutputStreamWriter} the standard output for the command
     * @throws IOException if files attempted to be opened cannot be 
     */
	public synchronized void eval(BufferedReader input, OutputStream output) throws IOException {
        
    
        String currentDirectory = directory.getCurrentDirectory();
        
        //String regex = "`(.*?)`";

        //command substitution
        Pattern pattern = Pattern.compile("`(.*?)`");
        Matcher matcher = pattern.matcher(rawCommand);
        
        if (matcher.find()){
          
            doCmdSub(input, output, matcher);
            return;
        }

        // return array of args - for each arg in arg evaluate that arg and then do any other args 
        
        ArrayList<String> args = tokenizeCommand(rawCommand, output);
        ArrayList<String> args1 = tokenizeCommand(rawCommand, output);

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
                    throw new RuntimeException("more than one I/O in same direction "  + nextArg);
                } else if (i == args.size() - 1) {
                    throw new RuntimeException(" no I/O file specified " + args.get(i));
                } else if (!new File(currentDirectory + File.separator  + nextArg).exists()) {
                    throw new RuntimeException("file does not exist "  + nextArg);
                } else {
                    inputFile = new File(currentDirectory + File.separator  + nextArg);
                    inputFileBool = true;
                    args1.remove(arg);
                    args1.remove(nextArg);
                }
            }else if (arg.equalsIgnoreCase(">")) {
                if (outputFileBool == true) {
                    throw new RuntimeException(" more than one I/O in same direction "  + nextArg);
                } else if (i == args.size() - 1) {
                    throw new RuntimeException(" no I/O file specified " + args.get(i));
                } else if (!new File(currentDirectory + File.separator  + nextArg).exists()) {
                    outputFile = new File(currentDirectory + File.separator + nextArg);
                    outputFile.createNewFile();
                    outputFileBool = true;
                    args1.remove(arg);
                    args1.remove(nextArg);
                } else if (new File(currentDirectory + File.separator  + nextArg).exists())
                {
                    outputFile = new File(currentDirectory + File.separator + nextArg);
                    outputFileBool = true;
                    args1.remove(arg);
                    args1.remove(nextArg);
                }
            }
        }
        if (outputFileBool == true) {
    
            output = new FileOutputStream(outputFile);
        }
        if (inputFileBool == true) {
            input = new BufferedReader(new FileReader(inputFile));
        }
        executeCommand(args1, input, output);
        }
           
 
    /**
     * Takes a String command and converts this into a List of tokens. 
     * @param rawCommands The raw unprocessed string to be converted into tokens
     * @param output {@code OutputStream} the Standard output for the app
     * @return ArrayList of tokens for the command
     * @throws IOException
     */
    public synchronized ArrayList<String> tokenizeCommand(String rawCommands, OutputStream output) throws IOException {
        glob glob_processor = new glob();
        ArrayList<String> args = glob_processor.get_tokens(rawCommands);
        return args;
    } 

    /**
     * Creates the application and calls exec on it
     * @param args The Arguments to be used bt the command
     * @param input The Standard input
     * @param output The standard output
     */
    public synchronized void executeCommand(ArrayList<String> args, BufferedReader input, OutputStream output) throws IOException {
        ApplicationFactory applicationFactory = new ApplicationFactory();
        ArrayList<String> appArgs = new ArrayList<String>();
        String appName = args.get(0);
        Boolean unsafe = (rawCommand.charAt(0) == '_')? true: false;
        appName = unsafe? appName.substring(1): appName;
        appArgs = new ArrayList<String>(args.subList(1, args.size()));
        Application command = applicationFactory.getApplication(appName, unsafe);
        command.exec(appArgs, input, new OutputStreamWriter(output));
        output.close();
    }

    /**
    * Runs command substitution on the application
    * @param input The Standard input
    * @param output The standard output
    * @param matcher The Regular Expression to find where the sub commands are
    * @throws IOException if cannot open file
    */
    public synchronized void doCmdSub(BufferedReader input, OutputStream output, Matcher matcher) throws IOException {
        
        ArrayList<String> cmdsubinput = new ArrayList<String>();
        String cmdsub = "";
        cmdsub = matcher.group();
        String command = cmdsub;
       
       
        cmdsub = cmdsub.replace("`", "");
       
        
        CommandSubstitution subcmd = new CommandSubstitution(cmdsub);
        cmdsubinput = subcmd.get_output(input);
        ArrayList<String> commandsubargs = new ArrayList<String>();
        

        for(String arg: cmdsubinput){
            String temp = rawCommand;
            temp = temp.replace(command, arg);
            commandsubargs = tokenizeCommand(temp, output);
           
            executeCommand(commandsubargs, input, output);
           
            
          
        }
            
        
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }


    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public BufferedReader getInput() {
        return input;
    }

    public OutputStream getOutput() {
        return output;
    }
    

}


