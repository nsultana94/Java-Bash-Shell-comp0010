package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.stream.Collectors;



public class CommandSubstitution {

    private String rawCommand;
  

    public CommandSubstitution(String cmdsub) {
        rawCommand = cmdsub;

    }

    public BufferedReader get_output(BufferedReader input) throws IOException {
        System.out.println(rawCommand);
        PipedOutputStream output = new PipedOutputStream();
        
        PipedInputStream in = new PipedInputStream(output);
        Call call = new Call(rawCommand);
        call.eval(input,output);
        output.flush();
        output.close();
        
        Reader reader = new InputStreamReader(in);
        
        BufferedReader input1 = new BufferedReader(reader);
        
        StringBuilder sb = new StringBuilder();
        String line = input1.readLine();
        while(line != null && line != ""){
            sb.append("\n");
            sb.append(line);
            line = input1.readLine();
        }
       
        
        System.out.println(sb.toString());
        Reader sr = new StringReader(sb.toString());
        BufferedReader cmdsubinput = new BufferedReader(sr);

        return cmdsubinput;

    }

}