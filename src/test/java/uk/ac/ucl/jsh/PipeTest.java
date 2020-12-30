package uk.ac.ucl.jsh;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PipeTest {
    PipedInputStream in;
    PipedOutputStream out;
    ArrayList<String> args;
    OutputStreamWriter output;
    BufferedReader input;
    CurrentDirectory directory = CurrentDirectory.getInstance();

/*
    @Before
    public void setup() throws Exception {
        in = new PipedInputStream();
        out = new PipedOutputStream(in);
        args = new ArrayList<String>();
        output = new OutputStreamWriter(out);
        input = new BufferedReader(new InputStreamReader(in));

    }

    @Test
    public void LsCat() throws IOException, InterruptedException {
        String command = "ls | cat";
        new pipe(command).eval(null, out);
        out.close();

        String result = input.lines().collect(Collectors.joining("\n"));

       
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

    @Test
    public void echoCat() throws IOException, InterruptedException {
        String command = "echo hello | cat";
        new pipe(command).eval(null, out);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "hello ");
    }

    @Test
    public void StartThrowsError() throws IOException, InterruptedException {
        String command = "ls nonExistantDir | cat";
        new pipe(command).eval(null, out);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void SecondIgnoresFirst() throws IOException, InterruptedException {
        String command = "pwd | echo hello";
        new pipe(command).eval(null, out);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "hello ");
    }

    @Test
    public void endThrowsError() throws IOException, InterruptedException {
        String command = "ls | head -n 0";
        new pipe(command).eval(null, out);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "");
    }

    @Test
    public void three() throws IOException, InterruptedException {
        String command = "ls | grep jsh | cut -b 1";
        new pipe(command).eval(null, out);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "j");
    }

  */  

}
