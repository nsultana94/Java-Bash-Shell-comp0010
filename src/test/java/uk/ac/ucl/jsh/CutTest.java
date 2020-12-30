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

public class CutTest{
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
    public void CutSingleIntervals() throws Exception {
       

        args.add("-b"); 
        args.add("1,2,3,4");
        args.add("testfile.txt");
        Cut cut = new Cut();
        cut.exec(args, null, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this\ntest\ntest\nfile");
    }

    @Test
    public void CutTwoIntervals() throws Exception {
        
        args.add("-b"); 
        args.add("-3,1-6");
        args.add("testfile.txt");
        Cut cut = new Cut();
        cut.exec(args, null, output);
        out.close();

        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this i\ntestin\ntest f\nfile");
    }

    @Test
    public void CutNoIntersectIntervals() throws Exception {
       

        args.add("-b"); 
        args.add("1-3,6-9");
        args.add("testfile.txt");
        Cut cut = new Cut();
        cut.exec(args, null, output);
        out.close();
       
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "thiis a\ntesng t\ntesfile\nfil");
    }

    @Test
    public void CutWholeInterval() throws Exception {
       

        args.add("-b"); 
        args.add("-3,6-");
        args.add("testfile.txt");
        Cut cut = new Cut();
        cut.exec(args, null, output);
        out.close();
  
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "thiis a test file\ntesng this\ntesfile\nfil");
    }

    @Test
    public void CutDifferentInterval() throws Exception {
       

        args.add("-b"); 
        args.add("1-3,1-9");
        args.add("testfile.txt");
        Cut cut = new Cut();
        cut.exec(args, null, output);
        out.close();
      
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a\ntesting t\ntest file\nfile");
    }

    @Test
    public void CutSingleInterval() throws Exception {
       

        args.add("-b"); 
        args.add("1-2");
        args.add("testfile2.txt");
        Cut cut = new Cut();
        cut.exec(args, null, output);
        
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "ap\nAp\nlo\ndo\ndo");
    }

    @Test
    public void CutStdIn() throws Exception {
       

        args.add("-b"); 
        args.add("1-2");

       
        BufferedReader reader = new BufferedReader(new FileReader("testfile2.txt"));

        Cut cut = new Cut();
        cut.exec(args, reader, output);
        
        out.close();
       
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "ap\nAp\nlo\ndo\ndo");
    }


    /* exceptions */
    @Test(expected = RuntimeException.class)
    public void CutTooManyArguments() throws Exception {
        

        args.add("-b"); 
        args.add("1-2");
        args.add("testfile2.txt");
        args.add("test");
        Cut cut = new Cut();
        cut.exec(args, null, output);
        out.close();
        
       
    }

    @Test(expected = RuntimeException.class)
    public void CutTooLittleArguments() throws Exception {
    

        args.add("-b"); 
        
        Cut cut = new Cut();
        cut.exec(args, null, output);
        out.close();

        
    }

    @Test(expected = RuntimeException.class)
    public void CutWrongOption() throws Exception {
      

        args.add("-n"); 
        args.add("1-2"); 
        args.add("testfile2.txt"); 
        
        Cut cut = new Cut();
        cut.exec(args, null, output);
        out.close();
        
        
    }

    @Test(expected = RuntimeException.class)
    public void CutWrongOverlap() throws Exception {
      

        args.add("-b"); 
        args.add("1-2--"); 
        args.add("testfile2.txt"); 
        
        Cut cut = new Cut();
        cut.exec(args, null, output);
        out.close();
    
        
    }
    @Test(expected = RuntimeException.class)
    public void CutFileDoesNotExist() throws Exception {
  

        args.add("-b"); 
        args.add("1-2"); 
        args.add("test.txt"); 
        
        Cut cut = new Cut();
        cut.exec(args, null, output);
        out.close();
          
    }

    


}