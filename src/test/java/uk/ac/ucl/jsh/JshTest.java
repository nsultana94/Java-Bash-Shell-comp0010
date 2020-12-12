package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

public class JshTest {
    public JshTest() {
    }

    @Test
    public void testJsh() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("echo foo", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(),"foo");
    }

     @Test
    public void testCut1() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b 1,2,3,4 testfile.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(),"this");
    } 
    /*
    @Test
    public void testCut2() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b -3,1-6 testfile.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(),"this i");
    }

    @Test
    public void testCut3() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b 1-3,4-9 testfile.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(),"this is a");
    }

    @Test
    public void testCut4() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b -3,6- testfile.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(),"thiis a test file");
    }
    @Test
    public void testCut5() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b 1-3,1-9 testfile.txt", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(),"this is a");
    } */
} 
