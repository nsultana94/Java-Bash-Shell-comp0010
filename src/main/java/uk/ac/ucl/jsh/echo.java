package uk.ac.ucl.jsh;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class echo implements Application {

    public echo(List<String> appArgs, String input, OutputStreamWriter writer) throws IOException {
        exec(appArgs,input,writer);
	}

    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException{
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