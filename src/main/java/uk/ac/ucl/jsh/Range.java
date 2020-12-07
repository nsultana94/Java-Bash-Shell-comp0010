package uk.ac.ucl.jsh;

/**
 * Class to contain a range item
 * Used in cut to store a range
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 * @see Cut
 */

public class Range{
    int start;
    int end;
    /**
     * Constructor to set start and end date
     * @param start the start value
     * @param end the end value
     */
    Range(int start, int end) 
    { 
        this.start=start; 
        this.end=end; 
    }
    /**
     * returns start value
     * @return start value
     */
    public int getStart(){
            return start;
    }
}