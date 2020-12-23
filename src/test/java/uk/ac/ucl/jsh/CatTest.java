package uk.ac.ucl.jsh;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CatTest{

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
    public void Cat() throws Exception {
        
        args.add("testfile.txt"); 
        args.add("testcmdsub.txt");
        cat cat = new cat();
        cat.exec(args, null ,output);

        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a test file\ntesting this\ntest file\nfile\ntestfile2.txt");
    }

    @Test
    public void CatStdin() throws Exception {
        

        String test = "testing";
        Reader inString= new StringReader(test);
        BufferedReader reader = new BufferedReader(inString);

        
        cat cat = new cat();
        cat.exec(new ArrayList<String>(), reader ,output);

        out.close();
       
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "testing");
    }

    /* exceptions */

    @Test(expected = RuntimeException.class)
    public void CatFileDoesNotExist() throws Exception {

        args.add("testfile.txt"); 
        args.add("test.txt");
        cat cat = new cat();
        cat.exec(args, null ,output);

        out.close();
       
    }

    
}