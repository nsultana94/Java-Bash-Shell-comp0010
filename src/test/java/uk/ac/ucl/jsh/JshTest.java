package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
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
    @Test(expected = RuntimeException.class)
    public void JshException() throws Exception {
        
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(), "");
    }

    @Test
    public void jshMain() throws Exception {
        Jsh.main(new String[] { "echo", "foo" });
        assertEquals("", outContent.toString());
    }

    @Test
    public void jshonearg() throws Exception {
        Jsh.main(new String[] { "echo" });
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
