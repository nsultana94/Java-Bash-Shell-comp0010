package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

public class pipe implements Command {
  private ArrayList<Call> calls = new ArrayList<Call>();

  public pipe(String rawCommand) throws IOException {
    for (String command : rawCommand.split("\\|")) {
      calls.add(new Call(command.trim()));
    }
  }

  public void eval(BufferedReader input, OutputStream output) throws IOException, InterruptedException {
    PipedOutputStream to_next = new PipedOutputStream();
    BufferedReader in = input;
    Call call;
    for (int i = 0; i < calls.size(); i++) {
      call = calls.get(i);
      // System.out.println(call.GetCommand());
      call.setInput(in);
      in = new BufferedReader(new InputStreamReader(new PipedInputStream(to_next)));
      if (i == calls.size() - 1) {
        call.setOutput(output);
      } else {
        call.setOutput(to_next);
        to_next = new PipedOutputStream();
      }
    }

    for (Call c : calls) {
      c.start();
      c.join();
    }
  }
}