package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class echo implements interface Application {
    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException;
}