package uk.ac.ucl.jsh;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class Cd implements Application {
    @Override
    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException {
        String currentDirectory = directory.getCurrentDirectory();

        if (args.isEmpty()) {
            throw new RuntimeException("cd: missing argument");
        } else if (args.size() > 1) {
            throw new RuntimeException("cd: too many arguments");
        }
        String dirString = args.get(0);
        File dir = new File(currentDirectory, dirString);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("cd: " + dirString + " is not an existing directory");
        }
        directory.SetCurrentDirectory(dir.getCanonicalPath());

    }
}