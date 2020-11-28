package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;

public class CommandSubstitution {

    private String rawCommand;
    private OutputStream output;
    private InputStream in;

    public CommandSubstitution(String cmdsub) {
        rawCommand = cmdsub;

    }

    public void get_output(BufferedReader input) throws IOException {
        PipedOutputStream output = new PipedOutputStream();
        InputStream in = new PipedInputStream(output);
        Call call = new Call(rawCommand);
        call.eval(input,output);
        System.out.println("yeet");
        Reader reader = new InputStreamReader(in);
        BufferedReader input1 = new BufferedReader(reader);
        System.out.println(input1.readLine());
        input1.close();
        
    
    }

}