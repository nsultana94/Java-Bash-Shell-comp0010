package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class Cut implements Application {
    public Cut() throws IOException {}

    @Override
    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException {
        String currentDirectory = directory.getCurrentDirectory();
        
        if (args.isEmpty()) {
            throw new RuntimeException("cut: missing arguments");
        }
        if(args.size() != 3){
            throw new RuntimeException("cut: wrong number of arguments");
        }
        if(!(args.get(0).equals("-b"))){
            throw new RuntimeException("cut: incorrect argument" + args.get(0));
        }
        if(!(args.get(1).equals("1,2,3")) && !(args.get(1).equals("1-3,5-7")) && !(args.get(1).equals("-3,5-"))){
            throw new RuntimeException("cut: wrong argument" + args.get(1));
        }

        String filename = args.get(2);
        File file = new File(currentDirectory + "/" + filename);
        
        if(file.exists()){

            try(  BufferedReader br = new BufferedReader(new FileReader(file))){
                String line;
                List<String> newLines = new ArrayList<>();
                while((line = br.readLine()) != null){
                    String cutline = "";
                    if(args.get(1).equals("1,2,3")){
                        cutline = line.substring(0,3);
                        newLines.add(cutline);
                    }
                    else if(args.get(1).equals("1-3,5-7")){
                        cutline = cutline.concat(line.substring(0,3)).concat(line.substring(4,7));
                        newLines.add(cutline);
                    }
                    else{
                        cutline = cutline.concat(line.substring(0,3)).concat(line.substring(4, line.length()));
                        newLines.add(cutline);
                    }
                }
                for(String lines: newLines){
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