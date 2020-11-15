package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;

public class Uniq implements Application {

    public Uniq() throws IOException {}

    @Override
    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException {
        String currentDirectory = directory.getCurrentDirectory();
        String filename;
        boolean options = false;

        if (args.isEmpty()) {
            throw new RuntimeException("uniq: missing arguments");
        }
        if(args.size() == 2 && !args.get(0).equals( "-i")){
            throw new RuntimeException("uniq: incorrect argument" + args.get(0));

        }
        if(args.size() > 2){
            throw new RuntimeException("uniq: incorrect number of arguments");
        }
        
        if(args.size() == 2){
            filename = args.get(1);
            options = true;
             }
        else{filename = args.get(0);}

        File file = new File(currentDirectory + "/" + filename);
        if(file.exists()){
          try(  BufferedReader br = new BufferedReader(new FileReader(file))){
            //creating list of lines in files
            String line = br.readLine(); 
            List<String> filelines = new ArrayList<>();
            List<String> uniqueFileLines = new ArrayList<>();
            
            while((line = br.readLine()) != null){
                filelines.add(line);  
            }
            
            // removing duplicates
            if (options){ //ignores case
                uniqueFileLines = filelines.stream().map(String::toLowerCase).distinct().collect(Collectors.toList());
            }
            else{
                uniqueFileLines = filelines.stream().distinct().collect(Collectors.toList());
            }
            for(String lines: uniqueFileLines){
                output.write(lines);
                output.write(System.getProperty("line.separator"));
                output.flush();
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Cannot open " + filename);
        }

    }
    else{
            throw new RuntimeException("file " + filename + " does not exist");
        }
    
}
}