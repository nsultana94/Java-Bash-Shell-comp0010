package uk.ac.ucl.jsh;


/**
 * Singleton class containing the current directory for the system
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */


public final class CurrentDirectory {
    private volatile String currentDirectory;
    private volatile static CurrentDirectory INSTANCE = null;

    /**
     * Constructor creating the object initialised with the starting directory
     * @param dir The starting Directory
     */

    private CurrentDirectory(String dir){
        currentDirectory = dir;
    }

    /**
     * Returns the current directory of the system
     * @return String of Current Directory
     */

    public String getCurrentDirectory(){
        return currentDirectory;
    }

    /**
     * Sets the current directory to be a set value
     * @param dir The Directory to set the current directory to
     */
    public void SetCurrentDirectory(String dir){
        currentDirectory = dir;
    }

    /**
     * Returns the current directory object 
     * @return the CurrentDirectory object
     */
    public static CurrentDirectory getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CurrentDirectory(System.getProperty("user.dir")); 
        }
        return INSTANCE;
    }
    
}
