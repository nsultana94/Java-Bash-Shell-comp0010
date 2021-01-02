package uk.ac.ucl.jsh;


/**
 * Singleton monitor class to store exceptions thrown
 * When one call in a pipe throws an exception the {@code Throwable} Object is stored in this class. 
 * The main class calling the pipe can then look at this class to see thee exception thrown
 * Allows for the exceptions to be handled safely from another thread to the one throwing the exception
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public final class ExceptionHolder {
    private volatile Throwable e;
    private static volatile ExceptionHolder INSTANCE = null;

    private ExceptionHolder(){
    }
    /**
     * Gets the exeception held in the class
     * @return {@code Throwable} Throwable thrown which is stored
     */
    public synchronized Throwable getException(){
        return e;
    }

    /**
     * Sets the exception to be stored
     * @param e {@code Throwable} to be stored
     */
    public synchronized void setThrowable(Throwable e){
        this.e = e;
    }

    /**
     * gets the instace of the singleton class
     * @return {@code ExceptionHolder} The instance of the class
     */
    public synchronized static ExceptionHolder getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ExceptionHolder(); 
        }
        return INSTANCE;
    }

    /**
     * Resets the stored exception to {@code null}
     * Call once a exception has been dealt with to ensure exceptions are not handled twice
     */
    public synchronized void reset(){
        this.e = null;
    }
    
}
