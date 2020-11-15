package uk.ac.ucl.jsh;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.regex.Pattern;

/*
The find command recursively searches for files with matching names. It outputs the list of relative paths, each followed by a newline:
find [PATH] -name PATTERN
•	PATTERN – file name with some parts replaced with * (asterisk).
•	PATH – the root directory for search. If not specified, use the current directory.

can be find -name PATTERN or find [PATH] -name PATTERN 
*/

public class find implements Application {

    public find() throws IOException {
    }

    @Override
    public void exec(List<String> args, String input, OutputStreamWriter output) throws IOException {
        String currentDirectory = directory.getCurrentDirectory();

        // throw exception if 0 arguments given
        if (args.isEmpty()) {
            throw new RuntimeException("find: missing arguments");
        }
        // throw exception if there is less than 2 or more than 3 arguments
        else if (!(args.size() == 3 || args.size() == 2)) {
            throw new RuntimeException("find: wrong arguments");
        }
        // where there are 2 arguments, throw exception if -name is not first argument
        else if ((args.size() == 3 && !args.get(0).equals("-name"))) {
            throw new RuntimeException("find: wrong arguments " + args.get(0));
        }
        // where there are 3 arguments, throw exception if -name is not second argument
        else if ((args.size() == 3 && !args.get(1).equals("-name"))) {
            throw new RuntimeException("find: wrong arguments " + args.get(1));
        }

        // if theres specified path and it exists, set targetDirectory to this. if not,
        // set targetDirectory as currentDirectory
        File targetDirectory;
        String patternArg;

        if (args.size() == 2) {
            targetDirectory = new File(currentDirectory);
            patternArg = args.get(1);
        } else {
            String pathArg = args.get(0);
            targetDirectory = new File(pathArg);
            if (!targetDirectory.exists()) {
                throw new RuntimeException("find: path does not exist " + args.get(0));
            }
        }
        patternArg = args.get(2);
        File[] targetDirListing = targetDirectory.listFiles();
        if (targetDirListing != null) {
            for (File child : targetDirListing) {
                if (Pattern.matches(patternArg, child.getName())) {
                    output.write(child.getName());
                    output.write(System.getProperty("line.separator"));
                    output.flush();
                }
            }
        } else {
            throw new RuntimeException("find: directory is empty");
        }
    }
}