package uk.ac.ucl.jsh;

import java.io.IOException;


public class ApplicationFactory {

    public  Application getApplication(String appName, Boolean unsafe) throws IOException {
        Application app;

        if(appName == null){
            return null;
        }
        else if(appName.equalsIgnoreCase("pwd")){
            app =  new Pwd();
        }
        
        else if(appName.equalsIgnoreCase("ls")){
            app =  new Ls();
        }
        else if(appName.equalsIgnoreCase("cat")){
            app =  new cat();
        }

        else if(appName.equalsIgnoreCase("echo")){
            app =  new echo();
        }

        else if(appName.equalsIgnoreCase("head")){
            app =  new head();
        }
        else if(appName.equalsIgnoreCase("tail")){
            app =  new tail();
        }
        else if(appName.equalsIgnoreCase("grep")){
            app =  new grep();
        }
        else if(appName.equalsIgnoreCase("cd")){
            app =  new Cd();
        }
        else if (appName.equalsIgnoreCase("sort")){
            app =  new Sort();
        }
        else{
            throw new RuntimeException(appName + ": unknown application");
        }
        return (unsafe? new UnsafeDecorator(app) : app);
    }
}