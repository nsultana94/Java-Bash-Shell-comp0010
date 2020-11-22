package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class tail implements Application {

    public tail() throws IOException {
	}

    
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{

        String currentDirectory = directory.getCurrentDirectory();
        if (args.size() > 3) {
            throw new RuntimeException("tail: wrong arguments");
        }
        if (args.size() == 3 && !args.get(0).equals("-n")) {
            throw new RuntimeException("tail: wrong argument " + args.get(0));
        }
        if (args.size() == 2 && !args.get(0).equals("-n")) {
            throw new RuntimeException("tail: wrong argument " + args.get(0));
        }

        
        int tailLines = 10;
        String tailArg;
        

        if(args.size() == 0 || args.size() == 2){
            if(args.size() == 0){
                tailLines = 10;
            }
            else{
                tailLines = get_tailLines(args.get(1));
            }
            write(input,output,tailLines);
        }else{
            if (args.size() == 3) {
                try {
                    tailLines = Integer.parseInt(args.get(1));
                } catch (Exception e) {
                    throw new RuntimeException("tail: wrong argument " + args.get(1));
                }
                tailArg = args.get(2);
            } else {
                tailArg = args.get(0);
            } 
            File tailFile = new File(currentDirectory + File.separator + tailArg);
            if (tailFile.exists()) {
                Charset encoding = StandardCharsets.UTF_8;
                Path filePath = Paths.get((String) currentDirectory + File.separator + tailArg);
                try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                    write(reader, output, tailLines);
                } catch (IOException e) {
                    throw new RuntimeException("tail: cannot open " + tailArg);
                }
            } else {
                throw new RuntimeException("tail: " + tailArg + " does not exist");
            }
        }
    }

    private void write(BufferedReader reader,OutputStreamWriter output,int tailLines) throws IOException {
        ArrayList<String> storage = new ArrayList<>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            storage.add(line);
        }
        int index = 0;
        if (tailLines > storage.size()) {
            index = 0;
        } else {
            index = storage.size() - tailLines;
        }
        for (int i = index; i < storage.size(); i++) {
            output.write(storage.get(i) + System.getProperty("line.separator"));
            output.flush();
        }
    }

    private int get_tailLines(String arg){
        try {
            int x =  Integer.parseInt(arg);
            if(x>0){
                return x;
            }else{
                throw new RuntimeException("tail: illegal line count " + arg);
            }
        } catch (Exception e) {
            throw new RuntimeException("tail: wrong argument " + arg);
        }
    }
}