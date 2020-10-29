package uk.ac.ucl.jsh;

import java.io.OutputStream;

public interface Application {
    public void exec(String args, String input, OutputStream output);
}