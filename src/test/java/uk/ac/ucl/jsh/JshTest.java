package uk.ac.ucl.jsh;

import org.junit.Test;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JshTest {
    CurrentDirectory directory = CurrentDirectory.getInstance();
    List<String> args = new ArrayList<String>();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testJsh() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("echo foo", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(), "foo");
    }


    @Test
    public void testJshMain() throws Exception {
        Jsh.main(new String[] {"-c","echo foo"});
        assertEquals("foo\n",outContent.toString());
    }

    @Test
    public void testJshMainExceptionWrongArgsNo() throws Exception {
        Jsh.main(new String[] {"-c"});
        assertEquals("jsh: wrong number of arguments\n",errContent.toString());
    }
    @Test
    public void testJshMainExceptionWrongArgs() throws Exception {
        Jsh.main(new String[] {"-d","echo foo"});
        assertEquals("jsh: -d: unexpected argument\n",errContent.toString());
    }
    @Test
    public void testJshMainErr() throws Exception {
        Jsh.main(new String[] {"-c","NonApp"});
        assertEquals("jsh: NonApp: unknown application\n",errContent.toString());
    }
    @Test
    public void testJshUser() throws Exception {
        CurrentDirectory currentDirectory = CurrentDirectory.getInstance();
        String prompt = currentDirectory.getCurrentDirectory() + "> ";
        BufferedReader input = new BufferedReader(new CharArrayReader("echo foo".toCharArray()));
        Jsh.doUserInput(input);
        assertEquals(prompt+ "foo\n",outContent.toString());
    }
    @Test
    public void testJshUserException() throws Exception {
        BufferedReader input = new BufferedReader(new CharArrayReader("NonApp".toCharArray()));
        Jsh.doUserInput(input);
        assertEquals("jsh: NonApp: unknown application\n",errContent.toString());
    }
    
    

    @Test(expected = RuntimeException.class)
    public void JshException() throws Exception {
        
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(), "");
    }

    /*@Test
    public void jshMain() throws Exception {
        Jsh.main(new String[] { "echo", "foo" });
        assertEquals("", outContent.toString());
    } */

    @Test
    public void jshonearg() throws Exception {
        Jsh.main(new String[] { "-c","echo" });
        assertEquals("", outContent.toString());

    }

    // runs forever : (
    // @Test
    // public void jshnoarg() throws Exception {
    // Jsh.main(new String[] {});

    // }

    // @Test
    // public void jshNoArg() throws Exception {
    // Jsh.main(new String[] {});
    // // BufferedReader input = new BufferedReader(new InputStreamReader(in));
    // // String result = input.lines().collect(Collectors.joining("\n"));
    // // assertEquals(result, "");
    // }
    /*
     * @Test(expected = RuntimeException.class) public void
     * CutExceptionNoArguments() throws Exception { PipedInputStream in = new
     * PipedInputStream(); PipedOutputStream out; out = new PipedOutputStream(in);
     * Jsh.eval("cut", out); out.close(); }
     */

}
