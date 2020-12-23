
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

public class SortTest{

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
    public void Sort() throws Exception {
        args.add("testfile2.txt");
        Sort sort = new Sort();
        sort.exec(args, null, output);
        out.close();

        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "Apple\napple\ndog\ndog\nlolly");
    }

    @Test
    public void SortReverse() throws Exception {
        args.add("-r");
        args.add("testfile2.txt");
        
        Sort sort = new Sort();
        sort.exec(args, null, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "lolly\ndog\ndog\napple\nApple");
    }

    @Test(expected = RuntimeException.class)
    public void SortFileDoesNotExist() throws Exception {
        args.add("-r");
        args.add("test.txt");
        Sort sort = new Sort();
        sort.exec(args, null, output);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void SortTooManyArguments() throws Exception {
        args.add("-r");
        args.add("test.txt");
        args.add("test.txt");
        Sort sort = new Sort();
        sort.exec(args, null, output);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void SortWrongArgument() throws Exception {
        args.add("-f");
        args.add("test.txt");
        Sort sort = new Sort();
        sort.exec(args, null, output);
        out.close();
    }

    @Test
    public void SortStdInReverse() throws Exception {
        args.add("-r");
        BufferedReader reader = new BufferedReader(new FileReader("testfile2.txt"));
        Sort sort = new Sort();
        sort.exec(args, reader, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "lolly\ndog\ndog\napple\nApple");
    }

    @Test
    public void SortStdIn() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("testfile2.txt"));
        Sort sort = new Sort();
        sort.exec(args, reader, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "Apple\napple\ndog\ndog\nlolly");
    }




    

}