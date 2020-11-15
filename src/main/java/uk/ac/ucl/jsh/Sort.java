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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Sort implements Application {

    public Sort() throws IOException {}

    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException {
        String currentDirectory = directory.getCurrentDirectory();

        if (args.isEmpty()) {
            throw new RuntimeException("sort: missing arguments");
        }
        if (!(args.size() <= 2)) {
            throw new RuntimeException("sort: wrong arguments");
        }
        if (args.size() == 2 && !args.get(0).equals("-r")) {
            throw new RuntimeException("sort: wrong argument " + args.get(0));
        }
        Boolean reverse = false;
        String fileArg; 
        if(args.size() == 2){
            reverse = true;
            fileArg = args.get(1);
        }else{
            fileArg = args.get(0);
        }
        File file = new File(currentDirectory + File.separator + fileArg);
        if (file.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get((String) currentDirectory + File.separator + fileArg);
            try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                ArrayList<String> lines = new ArrayList<String>();
                if(reverse){lines = (ArrayList<String>) reader.lines().sorted(Comparator.reverseOrder()).collect(Collectors.toList());}
                else {lines = (ArrayList<String>) reader.lines().sorted().collect(Collectors.toList());}
                for(String line:lines){
                    output.write(line);
                    output.write(System.getProperty("line.separator"));
                    output.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException("head: cannot open " + fileArg);
            }
        } else {
            throw new RuntimeException("head: " + fileArg + " does not exist");
        }


    }

    
}
