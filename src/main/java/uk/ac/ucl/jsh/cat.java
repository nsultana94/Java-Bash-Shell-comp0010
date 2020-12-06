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
import java.util.List;

public class cat implements Application {
    /**
     * Application implementing the JSH cat function.
     */

    public cat() throws IOException {}

    @Override
    /**
     * Runs the Cat function. Takes one or more files (or input if no files are provided) and writes their content to output
     * @param args The files to be read from
     * @param input standard input to be used if no files are provided
     * @param output standard output to be written to
     */
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{
        String currentDirectory = directory.getCurrentDirectory();
        BufferedReader reader;
        Charset encoding = StandardCharsets.UTF_8;
        
        if (args.isEmpty()) {
            reader = input;
            write(reader,output);
        } else {
            for (String arg : args) {
                File currFile = new File(currentDirectory + File.separator + arg);
                if (currFile.exists()) {
                    Path filePath = Paths.get(currentDirectory + File.separator + arg);
                    try{
                        reader = Files.newBufferedReader(filePath, encoding);
                        write(reader,output);
                    } catch (IOException e) {
                        throw new RuntimeException("cat: cannot open " + arg);
                    }
                } else {
                    throw new RuntimeException("cat: file does not exist");
                }
            }
        }
    }
   /*
   Private method used to write values from a buffered reader to the output
   Reader can either be the standard input or the output
   */
    private void write(BufferedReader reader,OutputStreamWriter output) throws IOException {
        String line = null;
        while ((line = reader.readLine()) != null) {
            output.write(String.valueOf(line));
            output.write(System.getProperty("line.separator"));
            output.flush();
        }
    }
}