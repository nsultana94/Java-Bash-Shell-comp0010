package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;

import java.util.ArrayList;




public class CommandSubstitution {

    private String rawCommand;
  

    public CommandSubstitution(String cmdsub) {
        rawCommand = cmdsub;

    }

    public ArrayList<String> get_output(BufferedReader input) throws IOException {
        System.out.println(rawCommand);
        PipedOutputStream output = new PipedOutputStream();
        ArrayList<String> result = new ArrayList<String>();
        
        PipedInputStream in = new PipedInputStream(output);
        Call call = new Call(rawCommand);
        call.eval(input,output);
        
        output.flush();
        output.close();
        
        Reader reader = new InputStreamReader(in);
        
        BufferedReader input1 = new BufferedReader(reader);
        
        //StringBuilder sb = new StringBuilder();
        String line = input1.readLine();
        while(line != null && line != ""){
            String[] arr = line.split(" ");  
            for(String word: arr){
                result.add(word);
            }  

            line = input1.readLine();
        }
       input1.close();
        
       // System.out.println(sb.toString());
       // Reader sr = new StringReader(sb.toString());
      //  BufferedReader cmdsubinput = new BufferedReader(sr);

        return result;

    }

}