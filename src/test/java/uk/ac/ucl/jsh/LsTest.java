package uk.ac.ucl.jsh;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import java.util.stream.Collectors;

public class LsTest{
    @Test
    public void Ls() throws Exception {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out;
        out = new PipedOutputStream(in);
        Jsh.eval("ls", out);
        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        CurrentDirectory directory = CurrentDirectory.getInstance();
        String CurrentDirectory = directory.getCurrentDirectory();
        File file = new File(CurrentDirectory);
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