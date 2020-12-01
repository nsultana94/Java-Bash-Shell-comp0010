package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;



public class Uniq implements Application {
    private boolean case_checking = true;

    public Uniq() throws IOException {}

    @Override
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{
        String currentDirectory = directory.getCurrentDirectory();
        String filename;
        BufferedReader br;

        if (args.isEmpty() || (args.size() == 1 && args.get(0).equals( "-i"))) {
            br = input;
            if(args.size() == 1){
                case_checking = false;
            }
        }else{
            if(args.size() == 2 && !args.get(0).equals( "-i")){
                throw new RuntimeException("uniq: incorrect argument" + args.get(0));
            }
            if(args.size() > 2){
                throw new RuntimeException("uniq: incorrect number of arguments");
            }
            
            if(args.size() == 2){
                filename = args.get(1);
                case_checking = false;
                }
            else{filename = args.get(0);}

            File file = new File(currentDirectory + "/" + filename);
            if(file.exists()){
                try{
                    br = new BufferedReader(new FileReader(file));
                }
                catch (IOException e) {
                    throw new RuntimeException("Cannot open " + filename);
                }
            }
            else{
                throw new RuntimeException("file " + filename + " does not exist");
            }
        }

        run_uniq(br, output);
    
    }

    private void run_uniq(BufferedReader br, OutputStreamWriter output) throws IOException {
         String line;
         String last_line="";

         
        while((line = br.readLine()) != null){
             if(case_checking && last_line.compareTo(line) != 0 ){
                write_line(line, output);
             }else if( !case_checking && last_line.toLowerCase().compareTo(line.toLowerCase()) != 0 ) {
                write_line(line, output);
             }
             last_line = line;
        }
   
    }       

    private void write_line(String line, OutputStreamWriter output) throws IOException {
        output.write(line);
        output.write(System.getProperty("line.separator"));
        output.flush();
    }

}