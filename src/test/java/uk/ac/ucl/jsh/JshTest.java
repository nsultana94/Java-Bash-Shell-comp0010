package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;


import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;


public class JshTest {
    CurrentDirectory directory = CurrentDirectory.getInstance();

    public JshTest() {
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
        assertEquals(scn.next(), "jsh cut: wrong number of arguments");
    }

    /*
     * @Test(expected = RuntimeException.class) public void
     * CutExceptionNoArguments() throws Exception { PipedInputStream in = new
     * PipedInputStream(); PipedOutputStream out; out = new PipedOutputStream(in);
     * Jsh.eval("cut", out); out.close(); }
     */


    

}
