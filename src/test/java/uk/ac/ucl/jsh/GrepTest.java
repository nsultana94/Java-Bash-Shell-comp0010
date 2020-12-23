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

public class GrepTest{
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
    public void Grep() throws Exception {
        args.add("test"); 
        args.add("testfile.txt");
        grep grep = new grep();
        grep.exec(args, null, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a test file\ntesting this\ntest file");
    }

    @Test
    public void GrepRegex() throws Exception {
        args.add("f+"); 
        args.add("testfile.txt");
        grep grep = new grep();
        grep.exec(args, null, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a test file\ntest file\nfile");
    }

    @Test
    public void GrepMultipleFiles() throws Exception {
        args.add("test"); 
        args.add("testfile.txt");
        args.add("testcmdsub.txt");

        grep grep = new grep();
        grep.exec(args, null, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "testfile.txt:this is a test file\ntestfile.txt:testing this\ntestfile.txt:test file\ntestcmdsub.txt:testfile2.txt");
    }

    @Test
    public void GrepStdIn() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("testfile.txt"));
        args.add("test"); 
        grep grep = new grep();
        grep.exec(args, reader, output);
        out.close();
    
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a test file\ntesting this\ntest file");
    }

    @Test(expected = RuntimeException.class)
    public void GrepTooLittleArguments() throws Exception {
        grep grep = new grep();
        grep.exec(args, null, output);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void GrepFileDoesNotExist() throws Exception {
        args.add("test"); 
        args.add("test.txt"); 
        grep grep = new grep();
        grep.exec(args, null, output);
        out.close();
    }

    


}