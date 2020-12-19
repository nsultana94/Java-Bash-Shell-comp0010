package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.util.stream.Collectors;

public class JshTest {
    CurrentDirectory directory = CurrentDirectory.getInstance();

    public JshTest() {
    }

    @Test
    public void testJsh() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("echo foo", out);
        Scanner scn = new Scanner(in);
        assertEquals(scn.next(), "foo");
    }

   
    /*
     * @Test(expected = RuntimeException.class) public void
     * CutExceptionNoArguments() throws Exception { PipedInputStream in = new
     * PipedInputStream(); PipedOutputStream out; out = new PipedOutputStream(in);
     * Jsh.eval("cut", out); out.close(); }
     */

    
    

    

    

    

    @Test
    public void testGrep() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("grep test testfile.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a test file\ntesting this\ntest file");
    }

    @Test
    public void testGrep2() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("grep f+ testfile.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a test file\ntest file\nfile");
    }

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

    @Test
    public void testCat() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cat testfile.txt testcmdsub.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a test file\ntesting this\ntest file\nfile\ntestfile2.txt");
    }

    @Test
    public void testSeq() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("uniq testfile2.txt; uniq testcmdsub.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nApple\nlolly\ndog\ntestfile2.txt");
    }

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
    public void IOexception1() throws Exception {
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
    public void IOexception2() throws Exception {
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
    public void IOexception3() throws Exception {
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
    public void OutputRedirectionexception1() throws Exception {
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
    public void OutputRedirectionexception2() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort > testfile.txt >", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void testFind1() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find testing -name find.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "/testing/find.txt");
    }

    @Test
    public void testFind2() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find -name find.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "./testing/find.txt");
    }

    /*@Test
    public void testFind3() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find -name *.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result,
                "/./output.txt\n./testing/output.txt\n/./output2.txt\n/./testcmdsub.txt\n/./testfile.txt\n/./testfile2.txt\n/./testhead.txt");
    }*/

    @Test
    public void testFind4() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find testing -name", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void testFind5() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find testing output.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void testFind6() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find invaliddirectory?!| -name", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void testFind7() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("find not_a_directory -name", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

}
