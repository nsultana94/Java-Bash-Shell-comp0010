package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CdTest {
    CurrentDirectory directory = CurrentDirectory.getInstance();

    PipedInputStream in;
    PipedOutputStream out;
    ArrayList<String> args;
    OutputStreamWriter output;
    BufferedReader input;

    @Before
    public void setup() throws Exception {
        in = new PipedInputStream();
        out = new PipedOutputStream(in);
        args = new ArrayList<String>();
        output = new OutputStreamWriter(out);
        input = new BufferedReader(new InputStreamReader(in));

    }

    @Test
    public void CdBasic() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cd testing", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void CdRoot() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cd ..", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }


    @Test(expected = RuntimeException.class)
    public void CdMissingArguments() throws Exception {
        Cd cd = new Cd();
        cd.exec(args, null, output);
        out.close(); 
    }

    @Test(expected = RuntimeException.class)
    public void CdTooManyArguments() throws Exception {
        args.add("testing");
        args.add("testing");
        Cd cd = new Cd();
        cd.exec(args, null, output);
        out.close(); 
    }

    @Test(expected = RuntimeException.class)
    public void CdDirectoryDoesNotEixts() throws Exception {
        args.add("testing1");
        Cd cd = new Cd();
        cd.exec(args, null, output);
        out.close(); 
    
    }

    
}