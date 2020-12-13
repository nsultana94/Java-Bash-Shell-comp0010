package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.util.stream.Collectors;

public class JshTest {
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
    

    @Test
    public void testCut1() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b 1,2,3,4 testfile.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this\ntest\ntest\nfile");
    }

    @Test
    public void testCut2() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b -3,1-6 testfile.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this i\ntestin\ntest f\nfile");
    }

    @Test
    public void testCut3() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b 1-3,6-9 testfile.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "thiis a\ntesng t\ntesfile\nfil");
    }

    @Test
    public void testCut6() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b -3,6- testfile.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "thiis a test file\ntesng this\ntesfile\nfil");
    }

    @Test
    public void testCut5() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b 1-3,1-9 testfile.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a\ntesting t\ntest file\nfile");
    }

    @Test
    public void testCut4() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("cut -b 1-2 testfile2.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "ap\nAp\nlo\ndo\ndo");
    }
 
    @Test
    public void testSort() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort testfile2.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "Apple\napple\ndog\ndog\nlolly");
    }
    
    

    @Test
    public void testSort2() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("sort -r testfile2.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "lolly\ndog\ndog\napple\nApple");
    }
    

    
    @Test
    public void testUniq() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("uniq testfile2.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nApple\nlolly\ndog");
    }

    @Test
    public void testUniq2() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("uniq -i testfile2.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nlolly\ndog");
    } 

    @Test
    public void testHead() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("head -n 2 testfile2.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nApple");
    }

    @Test
    public void testHead2() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("head testhead.txt", out);
        out.close();
        BufferedReader input =new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "a\nb\nc\nd\ne\nf\ng\nh\ni\nj");
    }
    @Test
    public void testHead3() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("head -n 20 testhead.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "a\nb\nc\nd\ne\nf\ng\nh\ni\nj\nk");
    }

    @Test
    public void testTail() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("tail -n 2 testhead.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "j\nk");
    }

    @Test
    public void testTail2() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("tail testhead.txt", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "b\nc\nd\ne\nf\ng\nh\ni\nj\nk");
    }
    @Test
    public void testCommandSubstituion() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("uniq `uniq testcmdsub.txt`", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "apple\nApple\nlolly\ndog");
    } 

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

   /* @Test
    public void testPwd() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("pwd", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        
        assertEquals(input.readLine(), CurrentDirectory.getInstance().toString());
    } */
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

    

    




}
