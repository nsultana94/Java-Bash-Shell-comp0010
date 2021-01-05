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
import java.util.List;

/**
 * Head function in JSH
 * takes a file and returns top n lines
 * if n is not specified the top 10 lines are returned
 * if no file is specified then standard input used
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class head implements Application {

    /**
     * Method to run the head application functionality.
     * @param args the arguments to be passed into the app.
     * @param input {@code BufferedReader} the standard input for the app
     * @param output {@code OutputStreamWriter} the standard output for the app
     */
    @Override
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException {
        String currentDirectory = directory.getCurrentDirectory();

        if (args.size() > 3) {
            throw new RuntimeException("head: wrong arguments");
        }
        if (args.size() == 3 && !args.get(0).equals("-n")) {
            throw new RuntimeException("head: wrong argument " + args.get(0));
        }
        if (args.size() == 2 && !args.get(0).equals("-n")) {
            throw new RuntimeException("head: wrong argument " + args.get(0));
        }
        
        int headLines;

        if(args.size() == 0 || args.size() == 2){
            if(args.size() == 0){
                headLines = 10;
            }
            else{
                headLines = get_headLines(args.get(1));
            }
            BufferedReader reader = input;
            write(reader,output,headLines);
        }else{
            String headArg;
            headLines = 10;
            if (args.size() == 3) {
                headLines = get_headLines(args.get(1));
                headArg = args.get(2);
            } else {
                headArg = args.get(0);
            }
            File headFile = new File(currentDirectory + File.separator + headArg);
            if (headFile.exists()) {
                Charset encoding = StandardCharsets.UTF_8;
                Path filePath = Paths.get((String) currentDirectory + File.separator + headArg);
                try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                    write(reader,output,headLines);
                } catch (IOException e) {
                    throw new RuntimeException("head: cannot open " + headArg);
                }
            } else {
                throw new RuntimeException("head: " + headArg + " does not exist");
            }
        }
    
    }
    
    /**
     * writes the top headLines lines to output
     * @param reader {@code BufferedReader} input - From a file or standard input
     * @param output {@code OutputStreamWriter} output to be written to
     * @param headLines number of lines to print
     * @throws IOException
     */
    private void write(BufferedReader reader,OutputStreamWriter output, int headLines) throws IOException {
        for (int i = 0; i < headLines; i++) {
            String line = null;
            if ((line = reader.readLine()) != null) {
                output.write(line);
                output.write(System.getProperty("line.separator"));
                output.flush();
            }
        }
    }


    /**
     * Gets the number of lines to be returned
     * @param arg the arguments to be processed
     * @return the number of lines to return
     */
    private int get_headLines(String arg){

            int x =  Integer.parseInt(arg);
            if(x>0){
                return x;
            }else{
                throw new RuntimeException("head: illegal line count " + arg);
            }
        } 
    }



