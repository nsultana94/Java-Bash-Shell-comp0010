package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class pipe implements Command {
    private Application app;
    private ArrayList<String> args;

    public pipe(String type, ArrayList<String> Args, Boolean unsafe) throws IOException {
        app = new ApplicationFactory().getApplication(type, unsafe);
        args = Args;
    }

    public void eval(BufferedReader input, OutputStream output) throws IOException {
      app.exec(args,input,new OutputStreamWriter(output)); 
    }

}