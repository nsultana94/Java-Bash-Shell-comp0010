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

/**
 * Application implementing the JSH cat function. 
 * Takes one or more files (or input if no files are provided) and writes their content to output 
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
*/
public class cat implements Application {


    public cat() throws IOException {}

    @Override
    /**
     * Runs the Cat function. Takes one or more files (or input if no files are provided) and writes their content to output
     * @param args The files to be read from
     * @param input {@code BufferedReader} standard input to be used if no files are provided
     * @param output {@code OutputStreamWriter} standard output to be written to
     * @throws IOException if files given are not able to be opened
     */
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{
        String currentDirectory = directory.getCurrentDirectory();
        BufferedReader reader;
        Charset encoding = StandardCharsets.UTF_8;
        
        if (args.isEmpty()) {
            write(input,output);
        } else {
            for (String arg : args) {
                File currFile = new File(currentDirectory + File.separator + arg);
                if (currFile.exists()) {
                    Path filePath = Paths.get(currentDirectory + File.separator + arg);
                    try{
                        reader = Files.newBufferedReader(filePath, encoding);
                    } catch (IOException e) {
                        throw new RuntimeException("cat: cannot open " + arg);
                    }
                    try{
                        write(reader,output);
                    }catch (IOException e){
                        System.out.println(e.getMessage());
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
        String line = "";
        while ((line = reader.readLine()) != null) {
            output.write(String.valueOf(line));
            output.write(System.getProperty("line.separator"));
            output.flush();
        }
   }
}