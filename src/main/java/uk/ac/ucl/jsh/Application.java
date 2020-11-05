package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public interface Application {
    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException;
    CurrentDirectory directory = new CurrentDirectory(System.getProperty("user.dir"));
}