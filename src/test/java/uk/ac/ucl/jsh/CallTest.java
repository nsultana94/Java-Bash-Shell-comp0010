package uk.ac.ucl.jsh;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CallTest{

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
    public void CallExecuteUnsafe() throws Exception {
        
        
        Call call = new Call("_cut");
        call.eval(null, out);
        out.close();
    
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "jsh cut: wrong number of arguments");
    }



}