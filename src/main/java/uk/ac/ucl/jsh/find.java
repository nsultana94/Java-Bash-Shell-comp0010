package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.regex.Pattern;

public class find implements Application {

    public find() throws IOException {
    }

    @Override
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException{
        String currentDirectory = directory.getCurrentDirectory();

        if (args.isEmpty()) {
            throw new RuntimeException("find: missing arguments");
        } else if (!(args.size() == 3 || args.size() == 2)) {
            throw new RuntimeException("find: wrong arguments");
        } else if ((args.size() == 2 && !args.get(0).equals("-name"))) {
            throw new RuntimeException("find: wrong arguments " + args.get(0));
        } else if ((args.size() == 3 && !args.get(1).equals("-name"))) {
            throw new RuntimeException("find: wrong arguments " + args.get(1));
        }

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
            patternArg = args.get(2);
        }

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