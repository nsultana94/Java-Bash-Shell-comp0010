package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class IOTest{
    PipedInputStream in;
    PipedOutputStream out;
    ArrayList<String> args;
    OutputStreamWriter output;
    BufferedReader input;
    CurrentDirectory directory = CurrentDirectory.getInstance();

    @Before
    public void setup() throws Exception {
        in = new PipedInputStream();
        out = new PipedOutputStream(in);
        args = new ArrayList<String>();
        output = new OutputStreamWriter(out);
        input = new BufferedReader(new InputStreamReader(in));

    }
    
    @Test
    public void testInputRedirection() throws Exception {
        Call call = new Call("sort < testfile2.txt");
        call.eval(null, out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "Apple\napple\ndog\ndog\nlolly");
    }

    @Test (expected = RuntimeException.class)
    public void IOexceptionTooManyInputRedirections() throws Exception {
        Call call = new Call("sort < testfile2.txt < test.txt");
        call.eval(null, out);
        out.close();
       
    }

    @Test(expected = RuntimeException.class)
    public void IOexceptionUnspecifiedFileRedirection() throws Exception {
        Call call = new Call("sort < testfile2.txt <");
        call.eval(null, out);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void IOexceptionFileNotExist() throws Exception {
        Call call = new Call("sort < test.txt");
        call.eval(null, out);
        out.close();
    }

    @Test
    public void testOutputRedirection() throws Exception {
       
        BufferedReader fileinput;
        Call call = new Call("sort testfile2.txt > output.txt");
        call.eval(null, out);
        out.close();
        String currentDirectory = directory.getCurrentDirectory();
        File file = new File(currentDirectory + File.separator + "output.txt");
        fileinput = new BufferedReader(new FileReader(file));
        String fileresult = fileinput.lines().collect(Collectors.joining("\n"));
        assertEquals(fileresult, "Apple\napple\ndog\ndog\nlolly");
    }

    @Test
    public void testOutputRedirectionfilecreation() throws Exception {
        BufferedReader fileinput;
        Call call = new Call("sort testfile2.txt > output2.txt");
        call.eval(null, out);
        out.close();
        String currentDirectory = directory.getCurrentDirectory();
        File file = new File(currentDirectory + File.separator + "output.txt");
        fileinput = new BufferedReader(new FileReader(file));
        String fileresult = fileinput.lines().collect(Collectors.joining("\n"));
        assertEquals(fileresult, "Apple\napple\ndog\ndog\nlolly");
        file.delete();

    }

    @Test(expected = RuntimeException.class)
    public void OutputRedirectionexceptionTooManyFiles() throws Exception {
        Call call = new Call("sort > testfile.txt > test.txt");
        call.eval(null, out);
        out.close();
    }

    @Test(expected = RuntimeException.class)
    public void OutputRedirectionExceptionUnspecifiedFile() throws Exception {
        Call call = new Call("sort > testfile.txt >");
        call.eval(null, out);
        out.close();
    }

}