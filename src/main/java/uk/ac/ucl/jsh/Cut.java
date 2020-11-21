package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
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

        // 
        String filename = args.get(2);
        File file = new File(currentDirectory + "/" + filename);
        
        if(file.exists()){

            try(  BufferedReader br = new BufferedReader(new FileReader(file))){
                String line;
                List<String> newLines = new ArrayList<>();
                while((line = br.readLine()) != null){
                    StringBuilder newline = new StringBuilder();
                    String arguments[] = args.get(1).split(",");
                    ArrayList<Range> intervals = new ArrayList<Range>();
                    
                    // splits the arguments by ,
                    
                for (int j = 0; j < arguments.length; j++){
                    char[] argumentchars = arguments[j].toCharArray();
                    for(int i = 0; i < argumentchars.length; i++){
                        int startindex = 0;
                        int lastindex = 0;
                        if(argumentchars.length == 1){
                            
                            startindex = Character.getNumericValue(argumentchars[i]-1);
                            lastindex = Character.getNumericValue(argumentchars[i]);
                        }
                        else if (argumentchars[i]=='-'){
                            if(i == 0){
                                startindex = 0;
                                lastindex = Character.getNumericValue(argumentchars[i+1]);
                            }
                            else if(i == argumentchars.length-1 ){
                                startindex = Character.getNumericValue(argumentchars[i-1]-1);
                                lastindex = line.length();
                            }
                            else{
                                lastindex = Character.getNumericValue(argumentchars[i+1]);
                                startindex = Character.getNumericValue(argumentchars[i-1]-1);
                            
                            }

                        }
                        
                        if(startindex < lastindex && startindex < line.length()){
                            Range interval = new Range(startindex, lastindex);
                            intervals.add(interval);
                        }
                    }
                }
                
                ArrayList<Range> finalintervals1 = overlapIntervals(intervals);

                for( Range r: finalintervals1 ){
                    if (r.end > line.length()){
                        r.end = line.length();
                    }
                    newline.append(line.substring(r.start, r.end));
                }
               // newLines.add(newline.toString());
                newLines.add(newline.toString());
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
    
    public ArrayList<Range> overlapIntervals(ArrayList<Range> intervals) {
        ArrayList<Range> finalintervals = new ArrayList<Range>();
        List<Range> sortedRange = intervals.stream()
                                        .sorted(Comparator.comparing(Range::getStart))
                                        .collect(Collectors.toList());
        int min = 0;
        int max = 0;
        int i = 0;
        for(Range r: sortedRange){

            if (r.start > max){
                if( i!= 0){
                    finalintervals.add(new Range(min, max));
                }
               
               min = r.start;
               max = r.end; 
            }
            else if(r.end >= max){
                max = r.end;
            }
            i++;
            
        }

        if(!finalintervals.contains(new Range(min, max))){ // first interval and last interval case
            finalintervals.add(new Range(min, max));
        }
        
        
        return finalintervals;



    }
}



