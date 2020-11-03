package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class ApplicationFactory {

    public  Application getApplication(String appName, List<String> args, String input, OutputStreamWriter output)
            throws IOException {
        if(appName == null){
            return null;
        }
        else if(appName.equalsIgnoreCase("pwd")){
            return new Pwd(args, input,output);
        }
        
        else if(appName.equalsIgnoreCase("ls")){
            return new Ls(args, input, output);
        }
        else if(appName.equalsIgnoreCase("cat")){
            return new cat(args, input, output);
        }

        else if(appName.equalsIgnoreCase("echo")){
            return new echo(args, input, output);
        }

        else if(appName.equalsIgnoreCase("head")){
            return new head(args, input, output);
        }
        else if(appName.equalsIgnoreCase("tail")){
            return new tail(args, input, output);
        }
        else if(appName.equalsIgnoreCase("grep")){
            return new grep(args, input, output);
        }
        else{
            throw new RuntimeException(appName + ": unknown application");
        }
        
        

}
}