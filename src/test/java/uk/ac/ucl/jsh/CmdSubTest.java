package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CmdSubTest{
    @Test
    public void testCommandSubstituion() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Call call = new Call("uniq `uniq testcmdsub.txt`");
        call.eval(null, out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nApple\nlolly\ndog");
    } 

    @Test
    public void testCommandSubstituionEcho() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        
        Call call = new Call("echo `echo foo`");
        call.eval(null, out);
        out.close();
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(), "foo");
        
}

@Test
    public void testCommandSubstitutionSemicolon() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("echo `echo foo; echo bar` hi", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "foo bar hi");
        
}
}