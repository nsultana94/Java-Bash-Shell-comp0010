package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a Pipe. The output of one command is passed into the input of the next
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class pipe implements Command {
  private List<Thread> calls = new ArrayList<Thread>();

  /**
   * Pipes commands together. The ourput of one command is passed into the input of the next
   * @param rawCommand The command to be split at pipe operators
   * @throws IOException
   */
  public pipe(String rawCommand) throws IOException {
    for (String command : rawCommand.split("\\|")) {
      calls.add(new Call(command.trim()));
    }
  }

  /**
   * runs the pipe. Sets up the pipedStreams and starts each command as a thread
   * @param input {@code BufferedReader} The input to the whole pipe
   * @param input {@code OutputStream} The output from the whole pipe
   */
  public void eval(BufferedReader input, OutputStream output) throws IOException, InterruptedException {
    PipedOutputStream to_next = new PipedOutputStream();
    BufferedReader in = input;
    Call call;
    for (int i = 0; i < calls.size(); i++) {
      call = (Call) calls.get(i);
      call.setInput(in);
      in = new BufferedReader(new InputStreamReader(new PipedInputStream(to_next)));
      if (i == calls.size() - 1) {
        call.setOutput(new SystemSafeStream(output));
      } else {
        call.setOutput(to_next);
        to_next = new PipedOutputStream();
      }
    }

    for (Thread c : calls) {
      c.start();
    }

    while(RunningTheads(calls)){
      for(Thread thread: calls){
          if(thread.isInterrupted()){
            intteruptAll(calls);
          }
        }
    }
    for (Thread c : calls) {
      c.join();
    }
  }

  /**
   * Private method to check if any {@code Thread} in a {@code List} of threads has been interupted
   * This will occour when they throw an exception adn intterupt themselves
   * @param threads A {@code List} of {@code Thread} objects
   * @return {@code true} if there are running threads, {@code false} if not
   */
  private boolean RunningTheads(List<Thread> threads){
    boolean result = false;
    for(Thread t:threads){
      result = result || t.isAlive();
    }
    return result;
  }
  
  private void intteruptAll(List<Thread> threads){
    for(Thread t: threads){
      t.interrupt();
    }
  }


  /**
   * A Output stream that is safe to use System.out in
   * Prevents closing System.out when closing other streams 
   * @author Saachi Pahwa
   * @author Naima Sultana 
   * @author Joshua Mukherjee
   */
  private class SystemSafeStream extends FilterOutputStream {

    public SystemSafeStream(OutputStream output) {
        super(output);
    }

    /**
     * Doesnt close the underlying Stream. Instead flushes it,
     * this is used to ensure {@code System.out} isnt closed
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        out.flush();
    }
  }

}