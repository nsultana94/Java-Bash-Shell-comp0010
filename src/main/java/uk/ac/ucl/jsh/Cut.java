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

/**
 * Class implementing JSH Cut application. 
 * Will take an input and cut certain bytes depending on the arguments
 * -b n,m,k extract 1st, 2nd and 3rd byte from each line.
 * -b 1-3,5-7 extract bytes from 1st to 3rd and from 5th to 7th from each line.
 * -b -3,5- extract bytes from the beginning of line to 3rd, and from 5th to the end of line from each line
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class Cut implements Application {
    public Cut() throws IOException {}

    /**
     * Method to run the Cut Application
     * @param args the arguments to be passed into the app
     * @param input {@code BufferedReader} the standard input for the app
     * @param output {@code OutputStreamWriter} the standard output for the app
     * @throws IOException
     * @see Range
     */
    @Override
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException {
        String currentDirectory = directory.getCurrentDirectory();

        String regex = "(-?[1-9]+[0-9]*-?([1-9]+[0-9]*)*-?)(,-?[1-9]+[0-9]*-?([1-9]+[0-9]*)*-?)*";
        // regex accepting formats of cut e.g 1,2,3 or 2-,3- or 1-2,2-3, or -2,3-
        
        
        if(args.size() > 2 && args.size() != 3){
            throw new RuntimeException("cut: wrong number of arguments");
        }
        if(args.size() < 2){
            throw new RuntimeException("cut: wrong number of arguments");
        }
        if(!(args.get(0).equals("-b"))){
            throw new RuntimeException("cut: incorrect argument" + args.get(0));
        }
        if(!(args.get(1).matches(regex))){
            throw new RuntimeException("cut: wrong argument " + args.get(1));
        }

        
        BufferedReader br;
        if(args.size() == 3){
            String filename = args.get(2);
            File file = new File(currentDirectory + "/" + filename);
            
            if(file.exists()){
                try{br = new BufferedReader(new FileReader(file));}
                catch (IOException e) {throw new RuntimeException("Cannot open " + filename);}
            }else{
                throw new RuntimeException("file " + filename + " does not exist");
            }
        }else{
            br = input;
        } 

        String line;
        List<String> newLines = new ArrayList<>();
        while((line = br.readLine()) != null){
            StringBuilder newline = new StringBuilder();
            String arguments[] = args.get(1).split(",");
            List<Range> intervals = new ArrayList<>();
            
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
        
            List<Range> finalintervals1 = overlapIntervals(intervals);

            for( Range r: finalintervals1 ){
                if (r.end > line.length()){
                    r.end = line.length();
                }
                newline.append(line.substring(r.start, r.end));
            }
        // newLines.add(newline.toString());
            newLines.add(newline.toString());
            for(String lines: newLines){
                output.write(lines);
                output.write(System.getProperty("line.separator"));
                output.flush();
            }
            newLines.clear(); 
        }
          
         
  
    }
    
    /**
     * Combines overlapping range objects
     * @param intervals {@code ArrayList} of {@code Range} Objects 
     * @return {@code ArrayList} of the resulting set of intervals
     * @see Range
     */
    public List<Range> overlapIntervals(List<Range> intervals) {
        List<Range> finalintervals = new ArrayList<>();
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



