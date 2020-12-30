package uk.ac.ucl.jsh;


/**
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */


public final class ExceptionHolder {
    private Throwable e;
    private static ExceptionHolder INSTANCE = null;

    private ExceptionHolder(){
    }

    public synchronized Throwable getException(){
        return e;
    }

    public synchronized void setThrowable(Throwable e){
        this.e = e;
    }

    public synchronized static ExceptionHolder getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ExceptionHolder(); 
        }
        return INSTANCE;
    }

    public synchronized void reset(){
        e = null;
    }
    
}
