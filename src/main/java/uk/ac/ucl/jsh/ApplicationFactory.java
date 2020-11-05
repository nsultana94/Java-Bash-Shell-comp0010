package uk.ac.ucl.jsh;

import java.io.IOException;


public class ApplicationFactory {

    public  Application getApplication(String appName)
            throws IOException {
        if(appName == null){
            return null;
        }
        else if(appName.equalsIgnoreCase("pwd")){
            return new Pwd();
        }
        
        else if(appName.equalsIgnoreCase("ls")){
            return new Ls();
        }
        else if(appName.equalsIgnoreCase("cat")){
            return new cat();
        }

        else if(appName.equalsIgnoreCase("echo")){
            return new echo();
        }

        else if(appName.equalsIgnoreCase("head")){
            return new head();
        }
        else if(appName.equalsIgnoreCase("tail")){
            return new tail();
        }
        else if(appName.equalsIgnoreCase("grep")){
            return new grep();
        }
        else if(appName.equalsIgnoreCase("cd")){
            return new Cd();
        }
        else{
            throw new RuntimeException(appName + ": unknown application");
        }
        
        

}
}