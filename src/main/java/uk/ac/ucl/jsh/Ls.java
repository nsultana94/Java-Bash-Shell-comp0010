package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Lists the files in the current directory
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */


public class Ls implements Application {

    public Ls() throws IOException {
	}

    /**
     * Lists files in current directory
     * @param args the arguments to be passed into the app. The Directory to be changed to
     * @param input {@code BufferedReader} the standard input for the app
     * @param output {@code OutputStreamWriter} the standard output for the app
     */

    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{
        String currentDirectory = directory.getCurrentDirectory();
        File currDir;
        if (args.isEmpty()) {
            currDir = new File(currentDirectory);
        } else if (args.size() == 1) {
            currDir = new File(args.get(0));
        } else {
            throw new RuntimeException("ls: too many arguments");
        }
        try {
            File[] listOfFiles = currDir.listFiles();
            boolean atLeastOnePrinted = false;
            for (File file : listOfFiles) {
                if (!file.getName().startsWith(".")) {
                    output.write(file.getName());
                    output.write("\t");
                    output.flush();
                    atLeastOnePrinted = true;
                }
            }
            if (atLeastOnePrinted) {
                output.write(System.getProperty("line.separator"));
                output.flush();
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("ls: no such directory");
        }
    }


}