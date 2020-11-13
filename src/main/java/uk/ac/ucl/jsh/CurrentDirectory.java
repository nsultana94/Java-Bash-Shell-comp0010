package uk.ac.ucl.jsh;

public final class CurrentDirectory {
    private String currentDirectory;
    private static CurrentDirectory INSTANCE = null;
    
    private CurrentDirectory(String dir){
        currentDirectory = dir;
    }

    public String getCurrentDirectory(){
        return currentDirectory;
    }

    public void SetCurrentDirectory(String dir){
        currentDirectory = dir;
    }

    public static CurrentDirectory getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CurrentDirectory(System.getProperty("user.dir")); 
        }
        return INSTANCE;
    }
    
}
