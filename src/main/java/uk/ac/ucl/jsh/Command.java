package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public interface Command {
    public void eval(BufferedReader input, OutputStream output) throws IOException;

    CurrentDirectory directory = CurrentDirectory.getInstance();
}