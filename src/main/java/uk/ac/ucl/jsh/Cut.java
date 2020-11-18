package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;





public class Cut implements Application {
    public Cut() throws IOException {}

    @Override
    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException {
        String currentDirectory = directory.getCurrentDirectory();

        String regex = "(-?[1-9]+[0-9]*-?([1-9]+[0-9]*)*-?)(,-?[1-9]+[0-9]*-?([1-9]+[0-9]*)*-?)*";
        // regex accepting formats of cut e.g 1,2,3 or 2-,3- or 1-2,2-3, or -2,3-
        
        if (args.isEmpty()) {
            throw new RuntimeException("cut: missing arguments");
        }
        if(args.size() != 3){
            throw new RuntimeException("cut: wrong number of arguments");
        }
        if(!(args.get(0).equals("-b"))){
            throw new RuntimeException("cut: incorrect argument" + args.get(0));
        }
        if(!(args.get(1).matches(regex))){
            throw new RuntimeException("cut: wrong argument " + args.get(1));
        }

        String filename = args.get(2);
        File file = new File(currentDirectory + "/" + filename);
        
        if(file.exists()){

            try(  BufferedReader br = new BufferedReader(new FileReader(file))){
                String line;
                List<String> newLines = new ArrayList<>();
                while((line = br.readLine()) != null){
                    String cutline = "";
                
                    String arguments[] = args.get(1).split(",");
                    System.out.println(arguments[1]);
                    // splits the arguments by ,
                    char[] argumentchars = arguments[1].toCharArray();
                    for(int i = 0; i < argumentchars.length; i++){
                        int startindex = 0;
                        int lastindex = 0;
                        if (argumentchars[i]=='-'){
                            if(i == 0){
                                startindex = 0;
                                lastindex = Character.getNumericValue(argumentchars[i+1]);
                            }
                            else if(i == argumentchars.length-1 ){
                                startindex = Character.getNumericValue(argumentchars[i-2]);
                                lastindex = line.length()-1;
                            }
                            else{
                                lastindex = Character.getNumericValue(argumentchars[i+1]);
                                startindex = Character.getNumericValue(argumentchars[i-1]);
                            }
                            if (lastindex)
                            System.out.println(argumentchars[i+1]);
                            System.out.println(argumentchars[i-1]);
                            cutline = cutline.concat(line.substring(startindex, lastindex));
                        }
                    }
                    newLines.add(cutline);
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