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

public class ApplicationFactoryTest{
    PipedInputStream in;
    PipedOutputStream out;
    ArrayList<String> args;
    OutputStreamWriter output;
    BufferedReader input;
    ApplicationFactory applicationfactory;
    CurrentDirectory directory = CurrentDirectory.getInstance();

    @Before
    public void setup() throws Exception {
        in = new PipedInputStream();
        out = new PipedOutputStream(in);
        args = new ArrayList<String>();
        output = new OutputStreamWriter(out);
        input = new BufferedReader(new InputStreamReader(in));
        applicationfactory = new ApplicationFactory();

    }

    @Test
    public void Pwd() throws Exception {
        Application app = applicationfactory.getApplication("pwd", false);
        app.exec(args,null,output);
        out.close();
        assertEquals(input.readLine(), directory.getCurrentDirectory());

    }

    @Test
    public void Ls() throws Exception {
        Application app = applicationfactory.getApplication("ls", false);
        app.exec(args,null,output);
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

    @Test
    public void Cat() throws Exception {
        Application app = applicationfactory.getApplication("cat", false);
        args.add("testfile.txt"); 
        args.add("testcmdsub.txt");
    
        app.exec(args, null ,output);

        out.close();
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a test file\ntesting this\ntest file\nfile\ntestfile2.txt");

    }

    @Test
    public void Head() throws Exception {
        Application app = applicationfactory.getApplication("head", false);
        args.add("testhead.txt");
        app.exec(args, null, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "a\nb\nc\nd\ne\nf\ng\nh\ni\nj");

    }

    @Test
    public void Tail() throws Exception {
        Application app = applicationfactory.getApplication("tail", false);
        args.add("testhead.txt");
        app.exec(args,null,output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "b\nc\nd\ne\nf\ng\nh\ni\nj\nk");

    }

    @Test
    public void Grep() throws Exception {
        Application app = applicationfactory.getApplication("grep", false);
        args.add("test"); 
        args.add("testfile.txt");
        app.exec(args, null, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this is a test file\ntesting this\ntest file");
    

    }

    @Test
    public void Cut() throws Exception {
        Application app = applicationfactory.getApplication("cut", false);
        args.add("-b"); 
        args.add("-3,1-6");
        args.add("testfile.txt");
        app.exec(args, null, output);
        out.close();
        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "this i\ntestin\ntest f\nfile");

    }
    @Test
    public void Find() throws Exception {
        Application app = applicationfactory.getApplication("find", false);
        args.add("testing"); 
        args.add("-name");
        args.add("'find.txt'");
        app.exec(args, null, output);
        out.close();

        String result = input.lines().collect(Collectors.joining("\n"));
        assertEquals(result, "/testing/find.txt");

    }

    @Test(expected = RuntimeException.class)
    public void InvalidApplication() throws Exception {
        Application app = applicationfactory.getApplication("test", false);
        

    }

    @Test(expected = RuntimeException.class)
    public void Unsafe() throws Exception {
        Application app = applicationfactory.getApplication("test", true);
        app.exec(args, null, output);
        

    }

    @Test
    public void NullAppName() throws Exception {
        Application app = applicationfactory.getApplication(null, false);
        assertNull(app);
    }

}