package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import java.util.stream.Collectors;

public class CdTest{
    CurrentDirectory directory = CurrentDirectory.getInstance();
    
   /* @Test
    public void Cd() throws Exception {
        Cd cd = new Cd ();
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        out = new PipedOutputStream(in);
        OutputStreamWriter output = new OutputStreamWriter(out);
        List<String> args = new ArrayList<String>();
        args.add("testing");
        cd.exec(args, input, output);
        String CurrentDirectory = directory.getCurrentDirectory();
        assertEquals((CurrentDirectory + "/" + "testing"), cd.getDirectory().getCurrentDirectory());
        }  */

        @Test
        public void CdMissingArguments() throws Exception {
            PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cd", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
        }

        @Test
        public void CdTooManyArguments() throws Exception {
            PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cd testing testing", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
        }

        @Test
        public void CdDirectoryDoesNotEixts() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cd testing1", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
        }


}