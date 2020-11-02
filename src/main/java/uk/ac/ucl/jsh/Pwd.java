package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class Pwd implements Application {
    
    private static String currentDirectory = System.getProperty("user.dir");

    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException {
        output.write(currentDirectory);
        output.write(System.getProperty("line.separator"));
        output.flush();

    }
}
