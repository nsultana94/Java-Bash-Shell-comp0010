
package uk.ac.ucl.jsh;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UnsafeDecoratorTest{
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
    public void UnsafeDecorator() throws Exception {
        UnsafeDecorator unsafe = new UnsafeDecorator(new Cut());
        unsafe.exec(args,null,output);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "jsh cut: wrong number of arguments");
    }

    @Test
    public void testSort() throws Exception {
        UnsafeDecorator unsafe = new UnsafeDecorator(new Sort());
        args.add("testfile2.txt");
        unsafe.exec(args,null,output);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "Apple\napple\ndog\ndog\nlolly");
    }


}
