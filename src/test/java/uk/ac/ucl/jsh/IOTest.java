package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import java.util.stream.Collectors;

public class IOTest{
    CurrentDirectory directory = CurrentDirectory.getInstance();

    @Test
    public void testInputRedirection() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort < testfile2.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "Apple\napple\ndog\ndog\nlolly");
    }

    @Test
    public void IOexceptionTooManyInputRedirections() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort < testfile2.txt < test.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void IOexceptionUnspecifiedFileRedirection() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort < testfile2.txt <", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void IOexceptionFileNotExist() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort < test.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void testOutputRedirection() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        BufferedReader fileinput;
        out = new PipedOutputStream(in);
        Jsh.eval("sort testfile2.txt > output.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String currentDirectory = directory.getCurrentDirectory();
        File file = new File(currentDirectory + File.separator + "output.txt");
        fileinput = new BufferedReader(new FileReader(file));
        String fileresult = fileinput.lines().collect(Collectors.joining("\n"));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(fileresult, "Apple\napple\ndog\ndog\nlolly");
    }

    @Test
    public void testOutputRedirectionfilecreation() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        BufferedReader fileinput;
        out = new PipedOutputStream(in);
        Jsh.eval("sort testfile2.txt > output2.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String currentDirectory = directory.getCurrentDirectory();
        File file = new File(currentDirectory + File.separator + "output2.txt");
        fileinput = new BufferedReader(new FileReader(file));
        String fileresult = fileinput.lines().collect(Collectors.joining("\n"));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(fileresult, "Apple\napple\ndog\ndog\nlolly");
    }

    @Test
    public void OutputRedirectionexceptionTooManyFiles() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort > testfile.txt > test.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void OutputRedirectionExceptionUnspecifiedFile() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort > testfile.txt >", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

}