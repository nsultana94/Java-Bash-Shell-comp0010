package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class cat implements Application {
    public void exec(String args, String input, OutputStream output) {

    }

    @Override
    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException {
        // TODO Auto-generated method stub

    }
}