package uk.ac.ucl.jsh;

import org.junit.Test;


import org.junit.Before;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GlobTest {

    PipedInputStream in;
    PipedOutputStream out;
    ArrayList<String> args;
    OutputStreamWriter output;
    BufferedReader input;
    List<String> result;

    @Before
    public void setup() throws Exception {
        in = new PipedInputStream();
        out = new PipedOutputStream(in);
        args = new ArrayList<String>();
        output = new OutputStreamWriter(out);
        input = new BufferedReader(new InputStreamReader(in));

    }

    @Test
    public void GlobDirectorySpecified() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("echo testing/*.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(), "testing/find.txt");

    }
    @Test
    public void singleQuotes() throws IOException{
        glob g = new glob();
        result = g.get_tokens("'foo'");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("foo");
        assertEquals(expected, result);
    }
    @Test
    public void doubleQuotes() throws IOException{
        glob g = new glob();
        result = g.get_tokens("\"foo\"");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("foo");
        assertEquals(expected, result);
    }
    @Test
    public void singleQuotesInside() throws IOException{
        glob g = new glob();
        result = g.get_tokens("foo'foo'foo");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("foofoofoo");
        assertEquals(expected, result);
    }
    @Test
    public void doubleQuotesInside() throws IOException{
        glob g = new glob();
        result = g.get_tokens("foo\"foo\"foo");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("foofoofoo");
        assertEquals(expected, result);
    }
    @Test
    public void singleQuotesSpace() throws IOException{
        glob g = new glob();
        result = g.get_tokens("foo' foo 'foo");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("foo foo foo");
        assertEquals(expected, result);
    }
    @Test
    public void doubleQuotesSpace() throws IOException{
        glob g = new glob();
        result = g.get_tokens("foo\" foo \"foo");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("foo foo foo");
        assertEquals(expected, result);
    }

}