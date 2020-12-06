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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Sort implements Application {
    /**
     * The Sort Application for JSH
     * Takes files or standard input and sorts the lines
     */
    private Boolean reverse = false;

    public Sort() throws IOException {}


    /**
     * Method to run sort
     * @param args Files and options to be used in the sorting
     * @param input standard input 
     * @param output standard output
     * @throws IOException if files passed in args cannot be opened
     * @throws RuntimeException if wrong number of arguments given or wrong options given
     */
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{
        String currentDirectory = directory.getCurrentDirectory();

        if ((args.size()>2)) {
            throw new RuntimeException("sort: wrong arguments");
        }
        if (args.size() == 2 && !args.get(0).equals("-r")) {
            throw new RuntimeException("sort: wrong argument " + args.get(0));
        }

        if(args.size() != 0 && (args.size() == 1 && !args.get(0).equals("-r"))){
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
                write_sorted(reader, output);
                } catch (IOException e) {
                    throw new RuntimeException("head: cannot open " + fileArg);
                }
            } else {
                throw new RuntimeException("head: " + fileArg + " does not exist");
            }
        }else{
            if(args.size() == 1){reverse = true;}
            write_sorted(input, output);
        }


    }

    /*
    Private method used to write values from a buffered reader to the output
    Reader can either be the standard input or the output
    */
    private void write_sorted(BufferedReader reader,OutputStreamWriter output) throws IOException {
         
        List<String> lines;
        if(reverse){lines = reader.lines().sorted(Comparator.reverseOrder()).collect(Collectors.toList());}
        else {lines = reader.lines().sorted().collect(Collectors.toList());}
        
        for(String line:lines){
            output.write(line);
            output.write(System.getProperty("line.separator"));
            output.flush();
        }

    }

    
}
