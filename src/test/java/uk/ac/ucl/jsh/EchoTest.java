package uk.ac.ucl.jsh;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.junit.*;

public class EchoTest {
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
    public void EchoTestSingle() throws IOException {
        args = new ArrayList<>();
        args.add("foo");
        echo echo = new echo();
        echo.exec(args, input ,output);

        out.close();
       
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "foo");
    }

    @Test
    public void EchoTestTwo() throws IOException {
        args = new ArrayList<>();
        args.add("foo");
        args.add("bar");

        echo echo = new echo();
        echo.exec(args, input ,output);

        out.close();
       
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "foo bar");
    }

    
    
    

    
}
