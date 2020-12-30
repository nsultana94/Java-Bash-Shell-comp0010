package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

// import uk.ac.ucl.jsh.pipe.StopEverythingException;

/**
 * Interface which will be implemented by classes to create command calls.
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public interface Command {
    /**
     * 
     * @param input {@code BufferedReader} Standard input for the call
     * @param output {@code OutputStreamWriter} Standard output for the call
     * @throws IOException 
     * @throws InterruptedException
     * @throws StopEverythingException
     */
    void eval(BufferedReader input, OutputStream output) throws IOException, InterruptedException;

    CurrentDirectory directory = CurrentDirectory.getInstance();
}