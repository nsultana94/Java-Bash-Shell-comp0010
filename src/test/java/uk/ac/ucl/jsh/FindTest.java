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

public class FindTest{
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
    public void FindDirectorySpecified() throws Exception {
        args.add("testing"); 
        args.add("-name");
        args.add("'find.txt'");
        find find = new find();
        find.exec(args, null, output);
        out.close();

        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "/testing/find.txt");
    }

    @Test
    public void FindDirectoryNotSpecified() throws Exception {
        args.add("-name");
        args.add("find.txt");
        find find = new find();
        find.exec(args, null, output);
        out.close();

        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "./testing/find.txt");
    }

    /*exceptions*/

    @Test(expected = RuntimeException.class)
    public void FindWrongOrderArgument() throws Exception {
        args.add("testing");
        args.add("-name");
        find find = new find();
        find.exec(args, null, output);
        out.close();
        
    }

    @Test(expected = RuntimeException.class)
    public void FindWrongArguments() throws Exception {
        args.add("testing");
        args.add("-test");
        args.add("'output.txt'");
        find find = new find();
        find.exec(args, null, output);
        out.close();
        
    }

    @Test(expected = RuntimeException.class)
    public void FindTooManyArguments() throws Exception {
        args.add("testing");
        args.add("-test");
        args.add("'output.txt'");
        args.add("find");
        find find = new find();
        find.exec(args, null, output);
        out.close();
        
    }

    @Test(expected = RuntimeException.class)
    public void FindTooLittleArguments() throws Exception {
        find find = new find();
        find.exec(args, null, output);
        out.close();
        
    }

    @Test(expected = RuntimeException.class)
    public void FindDirectoryDoesNotExist() throws Exception {
        args.add("not_a_directory");
        args.add("-name");
        args.add("'output.txt'");
        find find = new find();
        find.exec(args, null, output);
        out.close();
    }

}