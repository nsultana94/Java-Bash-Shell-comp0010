package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TailTest{

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
    public void Tail() throws Exception {
        tail tail = new tail();
        args.add("-n");
        args.add("2");
        args.add("testhead.txt");
        tail.exec(args,null,output);
        out.close();
    
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "j\nk");
    }

    

    @Test
    public void tail10Lines() throws Exception {
        tail tail = new tail();
        
        args.add("testhead.txt");
        tail.exec(args,null,output);
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "b\nc\nd\ne\nf\ng\nh\ni\nj\nk");
    }

    @Test
    public void tailLength() throws Exception {
        tail tail = new tail();
        args.add("-n");
        args.add("20");
        args.add("testhead.txt");
        tail.exec(args,null,output);
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk");
    }

    @Test
    public void TailStdInDefault() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("testhead.txt"));
        tail tail = new tail();
        tail.exec(args, reader, output);
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "b\nc\nd\ne\nf\ng\nh\ni\nj\nk");
    }

    @Test
    public void TailStdInSpecifiedLines() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("testhead.txt"));
        args.add("-n"); 
        args.add("2"); 
        tail tail = new tail();
        tail.exec(args, reader, output);
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "j\nk");
    }

    @Test(expected = RuntimeException.class)
    public void TailWrongNumberOfArguments() throws Exception {
    
        tail tail = new tail();
        args.add("-n");
        args.add("20");
        args.add("testhead.txt");
        args.add("20");
        tail.exec(args,null,output);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void TailWrongNumberArgumentFileIncluded() throws Exception {
        tail tail = new tail();
        args.add("-3");
        args.add("20");
        args.add("testhead.txt");
        tail.exec(args,null,output);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void TailWrongNumberArgument() throws Exception {
        tail tail = new tail();
        args.add("-3");
        args.add("20");
        tail.exec(args,null,output);
        out.close();
        
    }

    @Test(expected = RuntimeException.class)
    public void TailIllegalNumberCount() throws Exception {
        tail tail = new tail();
        args.add("-n");
        args.add("-2");
        args.add("testhead.txt");
        tail.exec(args,null,output);
        out.close();
        
    }


    @Test(expected = RuntimeException.class)
    public void TailFileNotExist() throws Exception {
        tail tail = new tail();
        args.add("-n");
        args.add("20");
        args.add("test.txt");
        tail.exec(args,null,output);
        out.close();
    }
}