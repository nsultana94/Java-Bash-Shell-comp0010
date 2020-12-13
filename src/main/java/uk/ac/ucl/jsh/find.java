package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.ArrayList;

/**
 * Class implementing the JSH Application find. Takes a pattern of file to match
 * and recursivly looks for files matching that pattern
 * 
 * @author Saachi Pahwa
 * @author Naima Sultana
 * @author Joshua Mukherjee
 */

public class find implements Application {

    /**
     * Method to recursivly find files matching a pattern
     * 
     * @param targetDirectory directory to look in
     * @param patternArg      Patterns to match to locate files
     * @param returnPath      Accumulator for path followed
     * @param returnPaths     Accumulator for files found
     * @return {@code ArrayList} of files found
     */

    public ArrayList<String> findFile(File targetDirectory, String patternArg, String returnPath,
            ArrayList<String> argReturnPaths) {
        File[] targetDirListing = targetDirectory.listFiles();
        if (targetDirListing != null) {
            for (File child : targetDirListing) {
                if (Pattern.matches(patternArg, child.getName())) {
                    argReturnPaths.add(returnPath + "/" + targetDirectory.getName() + "/" + child.getName());

                }
                if (returnPath.compareTo("") == 0) {
                    findFile(child, patternArg, targetDirectory.getName(), argReturnPaths);
                } else {
                    findFile(child, patternArg, returnPath + "/" + targetDirectory.getName(), argReturnPaths);
                }
            }
        }

        return argReturnPaths;
    }

    public find() throws IOException {
    }

    /**
     * Method to run the Find Application
     * 
     * @param args   the arguments to be passed into the app
     * @param input  {@code BufferedReader} the standard input for the app
     * @param output {@code OutputStreamWriter} the standard output for the app
     * @throws IOException
     */

    @Override
    public void exec(List<String> args, BufferedReader input, OutputStreamWriter output) throws IOException {
        // String currentDirectory = directory.getCurrentDirectory();
        if (args.isEmpty() || args.size() == 1) {
            throw new RuntimeException("find: missing arguments");
        } else if (args.size() == 2 && args.get(1).equals("-name")) {
            throw new RuntimeException("find: missing arguments");
        } else if (!(args.get(0).equals("-name")) && !(args.get(1).equals("-name"))) {
            throw new RuntimeException("find: missing argument -name");
        }

        else if (args.get(1).equals("-name")) {
            try {
                File firstArg = new File(args.get(1));
                firstArg.getCanonicalPath();
            } catch (IOException e) {
                throw new RuntimeException("find: directory path name is invalid");
            }
        }

        /*
         * else if (!(args.size() == 3 || args.size() == 2)) { throw new
         * RuntimeException("find: wrong arguments"); } else if ((args.size() == 2 &&
         * !args.get(0).equals("-name"))) { throw new
         * RuntimeException("find: wrong arguments " + args.get(0)); } else if
         * ((args.size() == 3 && !args.get(1).equals("-name"))) { throw new
         * RuntimeException("find: wrong arguments " + args.get(1)); }
         */
        // -name testfile.txt textfile2.txt

        File targetDirectory;
        ArrayList<String> patternArgs = new ArrayList<String>();

        if (args.get(0).equals("-name")) {
            targetDirectory = new File(".");
            for (int i = 1; i <= args.size() - 1; i++) {
                patternArgs.add(args.get(i));
            }
        } else {
            String pathArg = args.get(0);
            targetDirectory = new File(pathArg);
            if (!targetDirectory.exists()) {
                throw new RuntimeException("find: path does not exist " + args.get(0));
            }
            for (int i = 2; i <= args.size() - 1; i++) {
                patternArgs.add(args.get(i));
            }
        }

        if (targetDirectory.listFiles() == null) {
            throw new RuntimeException("find: directory is empty");
        }
        ArrayList<String> emptyPath = new ArrayList<String>();

        ArrayList<ArrayList<String>> allReturnPaths = new ArrayList<ArrayList<String>>();

        for (String patternArg : patternArgs) {
            allReturnPaths.add(findFile(targetDirectory, patternArg, "", emptyPath));
        }

        for (ArrayList<String> argReturnPaths : allReturnPaths) {
            for (String path : argReturnPaths) {
                output.write(path);
                output.write(System.getProperty("line.separator"));
                output.flush();
            }
        }
    }
}