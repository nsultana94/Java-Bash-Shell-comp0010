package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * The Echo Application in JSH.
 * Takes a input string and writes it to the output
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class echo implements Application {

    public echo() throws IOException {
	}
    /**
     * Method to run the echo Application
     * @param args the arguments to be passed into the app
     * @param input {@code BufferedReader} the standard input for the app
     * @param output {@code OutputStreamWriter} the standard output for the app
     * @throws IOException
     */
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{
        boolean atLeastOnePrinted = false;
        for (String arg : args) {
            if(atLeastOnePrinted){output.write(" ");}
            output.write(arg);
            output.flush();
            atLeastOnePrinted = true;
        }
        if (atLeastOnePrinted) {
            output.write(System.getProperty("line.separator"));
            output.flush();
        }
    };
}