package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * prints the current directory
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class Pwd implements Application {

    
    public Pwd() throws IOException {
	}


    /**
     * prints the current directory
     * @param args the arguments to be passed into the app. The Directory to be changed to
     * @param input {@code BufferedReader} the standard input for the app
     * @param output {@code OutputStreamWriter} the standard output for the app
     */
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{
        String currentDirectory = directory.getCurrentDirectory();
        output.write(currentDirectory);
        output.write(System.getProperty("line.separator"));
        output.flush();
    }
}
