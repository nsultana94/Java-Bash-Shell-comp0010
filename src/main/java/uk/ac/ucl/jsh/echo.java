package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class echo implements Application {
    /**
     * The Echo Application in JSH.
     * Takes a input string and writes it to the output
     */

    public echo() throws IOException {
	}
    /**
     * Takes a string input from args and writes it to the output
     * @param args The command line inputs to be passed to echo
     * @param input the standard input. Not used for echo
     * @param output standard output to be written to
     * @throws IOException if cannot write to output
     */
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{
        boolean atLeastOnePrinted = false;
        for (String arg : args) {
            output.write(arg);
            output.write(" ");
            output.flush();
            atLeastOnePrinted = true;
        }
        if (atLeastOnePrinted) {
            output.write(System.getProperty("line.separator"));
            output.flush();
        }
    };
}