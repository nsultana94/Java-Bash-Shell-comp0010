package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class Pwd implements Application {
    
    public Pwd() throws IOException {
	}


    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException {
        String currentDirectory = directory.getCurrentDirectory();
        output.write(currentDirectory);
        output.write(System.getProperty("line.separator"));
        output.flush();
    }
}
