package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class UnsafeDecorator implements Application {
    private Application app;

    public UnsafeDecorator(Application appIn){
        app = appIn;
    }

    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException {
        try{
            app.exec(args,input,output);
        }catch(Exception e){
            output.write("jsh "+e.getMessage());
            output.write(System.getProperty("line.separator"));
            output.flush();
        }
    }
}
