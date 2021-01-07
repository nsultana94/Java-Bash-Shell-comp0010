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

public class HeadTest{
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
    public void Head() throws Exception {
        args.add("-n"); 
        args.add("2"); 
        args.add("testfile2.txt");
        head head = new head();
        head.exec(args, null, output);
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nApple");
    }

   /* @Test
    public void HeadString() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("head -n \"2\" testfile2.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nApple");
    } */

    @Test
    public void HeadDefault() throws Exception {
    
        args.add("testhead.txt");
        head head = new head();
        head.exec(args, null, output);
        out.close();


        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "a\nb\nc\nd\ne\nf\ng\nh\ni\nj");
    }

    @Test
    public void HeadLength() throws Exception {
        args.add("-n");
        args.add("20");
        args.add("testhead.txt");
        head head = new head();
        head.exec(args, null, output);
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk");
    }

    @Test
    public void HeadStdInDefault() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("testhead.txt"));
        head head = new head();
        head.exec(args, reader, output);
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "a\nb\nc\nd\ne\nf\ng\nh\ni\nj");
    }

    @Test
    public void HeadStdInSpecifiedLines() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("testfile2.txt"));
        args.add("-n"); 
        args.add("2"); 
        head head = new head();
        head.exec(args, reader, output);
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nApple");
    }



    /*exceptions*/

    @Test(expected = RuntimeException.class)
    public void HeadWrongNumberOfArguments() throws Exception {
        args.add("-n");
        args.add("20");
        args.add("testhead.txt");
        args.add("20");
        head head = new head();
        head.exec(args, null, output);
        out.close();
        
    }

    @Test(expected = RuntimeException.class)
    public void HeadWrongArguments() throws Exception {
        args.add("-4");
        args.add("20");
        head head = new head();
        head.exec(args, null, output);
        out.close();
        
    }

    @Test(expected = RuntimeException.class)
    public void HeadWrongNumberArgumentsFileIncluded() throws Exception {
        args.add("-n");
        args.add("testhead.txt");
        head head = new head();
        head.exec(args, null, output);
        out.close();
       
    }

    @Test(expected = RuntimeException.class)
    public void HeadWrongOption() throws Exception {
        args.add("-3");
        args.add("20");
        args.add("testhead.txt");
        head head = new head();
        head.exec(args, null, output);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void HeadIllegalNumberCount() throws Exception {
        args.add("-n");
        args.add("-2");
        args.add("testhead.txt");
        head head = new head();
        head.exec(args, null, output);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void ZeroNumberCount() throws Exception {
        args.add("-n");
        args.add("0");
        args.add("testhead.txt");
        head head = new head();
        head.exec(args, null, output);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void HeadWrongNumberArguments() throws Exception {
        args.add("-n");
        args.add("two");
        args.add("testhead.txt");
        head head = new head();
        head.exec(args, null, output);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void HeadFileNotExist() throws Exception {
        args.add("-n");
        args.add("20");
        args.add("test.txt");
        head head = new head();
        head.exec(args, null, output);
        out.close();
    }
}
