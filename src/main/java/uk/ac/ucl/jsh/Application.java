package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Application interface, used to define the functionality of the JSH shell applications
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public interface Application {
    /**
     * Runs the main functionality for the application
     * @param args The input from the command line 
     * @param input {@code BufferedReader} The standard input
     * @param output {@code OutputStreamWriter} The standard output
     * @throws IOException
     */
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException;
    CurrentDirectory directory = CurrentDirectory.getInstance(); 
}