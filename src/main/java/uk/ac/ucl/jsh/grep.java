package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



public class grep implements Application {
    private int numOfFiles;

    public grep() throws IOException {
    }

    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{
        String currentDirectory = directory.getCurrentDirectory();
        if (args.size() == 0) {
            throw new RuntimeException("grep: wrong number of arguments");
        }
        Pattern grepPattern = Pattern.compile(args.get(0));
        numOfFiles = args.size() - 1;
        Path filePath;
        Path[] filePathArray = new Path[numOfFiles];
        Path currentDir = Paths.get(currentDirectory);
        if(numOfFiles > 0){
            for (int i = 0; i < numOfFiles; i++) {
                filePath = currentDir.resolve(args.get(i + 1));
                if (Files.notExists(filePath) || Files.isDirectory(filePath) || !Files.exists(filePath)
                        || !Files.isReadable(filePath)) {
                    throw new RuntimeException("grep: wrong file argument");
                }
                filePathArray[i] = filePath;
            }
            for (int j = 0; j < filePathArray.length; j++) {
                Charset encoding = StandardCharsets.UTF_8;
                try (BufferedReader reader = Files.newBufferedReader(filePathArray[j], encoding)) {
                    match(reader,output,grepPattern,args.get(j+1));
                } catch (IOException e) {
                    throw new RuntimeException("grep: cannot open " + args.get(j + 1));
                }
            }
        }else{
            match(input, output, grepPattern, "");
        }
    }

    private void match(BufferedReader reader,OutputStreamWriter output, Pattern grepPattern,String fileName)
            throws IOException {
        String line = null;
        while ((line = reader.readLine()) != null) {
            Matcher matcher = grepPattern.matcher(line);
            if (matcher.find()) {
                if (numOfFiles > 1) {
                    output.write(fileName);
                    output.write(":");
                }
                output.write(line);
                output.write(System.getProperty("line.separator"));
                output.flush();
            }
        }
    }

}