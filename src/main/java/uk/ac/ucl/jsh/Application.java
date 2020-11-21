package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public interface Application {
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException;
    CurrentDirectory directory = CurrentDirectory.getInstance();
}