package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Unsafe Decorator to provide unsafe functionality to an application
 * Catched exceptions and writes them to the output instead of throwing the error
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class UnsafeDecorator implements Application {
    private Application app;

    /**
     * Constructor 
     * @param appIn App to decorate
     */
    public UnsafeDecorator(Application appIn){
        app = appIn;
    }
    
    /**
     * Method to run the app and catching the errors
     * @param args the arguments to be passed into the app.
     * @param input {@code BufferedReader} the standard input for the app
     * @param output {@code OutputStreamWriter} the standard output for the app
     */
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException {
        try{
            app.exec(args,input,output);
        }catch(Exception e){
            output.write("jsh "+e.getMessage());
            output.write(System.getProperty("line.separator"));
            output.flush();
        }
    }
}
