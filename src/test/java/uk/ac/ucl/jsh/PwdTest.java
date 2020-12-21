package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;


public class PwdTest{
    CurrentDirectory directory = CurrentDirectory.getInstance();

    @Test
    public void testPwd() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("pwd", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        assertEquals(input.readLine(), directory.getCurrentDirectory());
    }
}