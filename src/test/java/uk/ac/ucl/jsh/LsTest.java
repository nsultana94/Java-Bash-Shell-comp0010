package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class LsTest{

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
    public void Ls() throws Exception {
        Ls ls = new Ls();
        ls.exec(new ArrayList<String>(), null, output);
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        
        String CurrentDirectory = directory.getCurrentDirectory();
        File file = new File(CurrentDirectory);
        String[] files = file.list();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < files.length; i++){
            if(!(files[i].startsWith("."))){
                sb.append(files[i]);
                if(i!= files.length -1)
                {
                    sb.append("\n");
                }
            } 
        }
        
        assertEquals(result, sb.toString());
    }

    @Test(expected = RuntimeException.class)
    public void LsTooManyArguments() throws Exception {
        args.add("test");
        args.add("test2");
        Ls ls = new Ls();
        ls.exec(args, null, output);
        out.close();
         
    }

    @Test(expected = RuntimeException.class)
    public void LsDirectoryDoesNotExist() throws Exception {
        args.add("test");
        Ls ls = new Ls();
        ls.exec(args, null, output);
        out.close();
    }

    @Test
    public void LsDirectorySpecified() throws Exception {
        args.add("testing");
        Ls ls = new Ls();
        ls.exec(args, null, output);
        out.close();
        
        String result = input.lines().collect(Collectors.joining("\n"));
        
        String CurrentDirectory = directory.getCurrentDirectory();
        File file = new File(CurrentDirectory + "/" + "testing");
        String[] files = file.list();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < files.length; i++){
            if(!(files[i].startsWith("."))){
                System.out.println(files[i]);
                sb.append(files[i]);
                if(i!= files.length -1)
                {
                    sb.append("\n");
                }
            } 
        }
        
        assertEquals(result, sb.toString());
    }
    



}