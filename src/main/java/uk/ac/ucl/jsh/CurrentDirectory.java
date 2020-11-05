package uk.ac.ucl.jsh;

public class CurrentDirectory {
    private String currentDirectory;
    
    public CurrentDirectory(String dir){
        currentDirectory = dir;
    }

    public String getCurrentDirectory(){
        return currentDirectory;
    }

    public void SetCurrentDirectory(String dir){
        currentDirectory = dir;
    }
    
}
