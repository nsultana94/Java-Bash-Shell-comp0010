package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;

import java.util.ArrayList;



/**
 * Class implementing command substitution
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class CommandSubstitution {

    private String rawCommand;
    /**
     * Constructor to take String and process it to run command substitution  
     * @param cmdsub String input
     */

    public CommandSubstitution(String cmdsub) {
        rawCommand = cmdsub;
    }
    
    /**
     * gets the output from command substitution
     * 
     * @param input {@code BufferedReader} representing the standard input
     * @return {@code ArrayList}
     * @throws IOException          if output writing fails
     * @throws InterruptedException
     */

    public synchronized ArrayList<String> get_output(BufferedReader input) throws IOException, InterruptedException {
       
     
        ArrayList<String> result = new ArrayList<String>();
        
    
        String[] commands = rawCommand.split(";", -1);
        for(int i = 0; i < commands.length; i++){
        PipedOutputStream output = new PipedOutputStream();
        PipedInputStream in = new PipedInputStream(output);
        pipe call = new pipe(commands[i]);
        call.eval(input,output);
        
        output.flush();
        output.close();
        
        Reader reader = new InputStreamReader(in);
        
        BufferedReader input1 = new BufferedReader(reader);
        
        String line = input1.readLine();
        while(line != null && line != ""){
            String[] arr = line.split(" ");  
            for(String word: arr){
                result.add(word);
            } 
            

            line = input1.readLine();
        }
       input1.close();

        }
        
            
        return result;

    }

}