
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

public class UniqTest{

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
    public void testUniqCaseSensitive() throws Exception {
        Uniq uniq = new Uniq();
        args.add("testfile2.txt");
        uniq.exec(args, null, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nApple\nlolly\ndog");
    }

    @Test
    public void testUniq() throws Exception {
        Uniq uniq = new Uniq();
        args.add("-i");
        args.add("testfile2.txt");
        uniq.exec(args, null, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nlolly\ndog");
    }

    @Test
    public void UniqStdInCaseSensitive() throws Exception {
        Uniq uniq = new Uniq();
        BufferedReader reader = new BufferedReader(new FileReader("testfile2.txt"));
        uniq.exec(args, reader, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nApple\nlolly\ndog");
    }

    @Test
    public void UniqStdIn() throws Exception {
        Uniq uniq = new Uniq();
        args.add("-i");
        BufferedReader reader = new BufferedReader(new FileReader("testfile2.txt"));
        uniq.exec(args, reader, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nlolly\ndog");
    }




    @Test(expected = RuntimeException.class)
    public void UniqWrongOption() throws Exception {
        Uniq uniq = new Uniq();
        args.add("-n");
        args.add("testfile2.txt");
        uniq.exec(args, null, output);
        out.close();
      
        
    }
    @Test(expected = RuntimeException.class)
    public void UniqWrongNumberOfArguments() throws Exception {
        Uniq uniq = new Uniq();
        args.add("-i");
        args.add("testfile2.txt");
        args.add("testfile2.txt");
        uniq.exec(args, null, output);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void UniqFileDoesNotExist() throws Exception {
        Uniq uniq = new Uniq();
        args.add("-i");
        args.add("test.txt");
        
        uniq.exec(args, null, output);
        out.close();
    }


}