package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Tail function in JSH
 * takes a file and returns bottom n lines
 * if n is not specified the bottom 10 lines are returned
 * if no file is specified then standard input used
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class tail implements Application {

    public tail() throws IOException {
	}

    /**
     * Function to run the tail application functionality.
     * @param args the arguments to be passed into the app.
     * @param input {@code BufferedReader} the standard input for the app
     * @param output {@code OutputStreamWriter} the standard output for the app
     */
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{

        String currentDirectory = directory.getCurrentDirectory();
        if (args.size() > 3) {
            throw new RuntimeException("tail: wrong arguments");
        }
        if (args.size() == 3 && !args.get(0).equals("-n")) {
            throw new RuntimeException("tail: wrong argument " + args.get(0));
        }
        if (args.size() == 2 && !args.get(0).equals("-n")) {
            throw new RuntimeException("tail: wrong argument " + args.get(0));
        }

        
        int tailLines = 10;
        String tailArg;
        

        if(args.size() == 0 || args.size() == 2){
            if(args.size() == 0){
                tailLines = 10;
            }
            else{
                tailLines = get_tailLines(args.get(1));
            }
            write(input,output,tailLines);
        }else{
            if (args.size() == 3) {
                tailLines = get_tailLines(args.get(1));
                tailArg = args.get(2);
            } else {
                tailArg = args.get(0);
            } 
            File tailFile = new File(currentDirectory + File.separator + tailArg);
            if (tailFile.exists()) {
                Charset encoding = StandardCharsets.UTF_8;
                Path filePath = Paths.get((String) currentDirectory + File.separator + tailArg);
                try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                    write(reader, output, tailLines);
                } catch (IOException e) {
                    throw new RuntimeException("tail: cannot open " + tailArg);
                }
            } else {
                throw new RuntimeException("tail: " + tailArg + " does not exist");
            }
        }
    }

    /**
     * writes the bottom tailLines lines to output
     * @param reader {@code BufferedReader} input - From a file or standard input
     * @param output {@code OutputStreamWriter} output to be written to
     * @param tailLines number of lines to print
     * @throws IOException
     */
    private void write(BufferedReader reader,OutputStreamWriter output,int tailLines) throws IOException {
        ArrayList<String> storage = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            storage.add(line);
        }
        int index = 0;
        if (tailLines > storage.size()) {
            index = 0;
        } else {
            index = storage.size() - tailLines;
        }
        for (int i = index; i < storage.size(); i++) {
            output.write(storage.get(i) + System.getProperty("line.separator"));
            output.flush();
        }
    }

    /**
     * Gets the number of lines to be returned
     * @param arg the arguments to be processed
     * @return the number of lines to return
     */
    private int get_tailLines(String arg){
        try {
            int x =  Integer.parseInt(arg);
            if(x>0){
                return x;
            }else{
                throw new RuntimeException("tail: illegal line count " + arg);
            }
        } catch (Exception e) {
            throw new RuntimeException("tail: wrong argument " + arg);
        }
    }
}