package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * Method to walk through directory and find all files matching a pattern
     *
     * @param targetDirectory directory to look in
     * @param patternArg      Pattern to match to locate files
     * 
     * @return {@code ArrayList} of file paths found
     */

    public List<String> findPattern(String patternArg, File targetDirectory) throws IOException {
        String searchPath = patternArg.replaceAll("\\*", ".*");
        if (!searchPath.contains("/")) {
            searchPath = "/" + searchPath;
        }
        Pattern regex = Pattern.compile(searchPath);

        try (Stream<Path> walk = Files.walk(targetDirectory.toPath())) {
            return walk.filter(path -> doesPathMatch(path, regex)).map(Path::toString).collect(Collectors.toList());
        }
    }

    /**
     * Method to return whether a path name matches a regex
     *
     * @param path  path that is being compared
     * @param regex regex to compare to path
     * @return {@code ArrayList} of files found
     */

    private boolean doesPathMatch(Path path, Pattern regex) {
        Matcher matcher = regex.matcher(path.toString());
        ;
        if (matcher.find()) {
            return matcher.group(0) != null;
        }
        return false;
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

        if (args.isEmpty()) {
            throw new RuntimeException("find: missing arguments");
        } else if (!(args.size() == 3 || args.size() == 2)) {
            throw new RuntimeException("find: wrong number of arguments");
        } else if (args.size() == 2 && !args.get(0).equals("-name")) {
            throw new RuntimeException("find: wrong argument " + args.get(0));
        } else if (args.size() == 3 && !args.get(1).equals("-name")) {
            throw new RuntimeException("find: wrong argument " + args.get(1));
        }

        File targetDirectory;
        String patternArg = "";

        if (args.size() == 2) {
            targetDirectory = new File("./");
            patternArg = args.get(1);
        } else {
            String pathArg = args.get(0);
            targetDirectory = new File(pathArg);
            if (!targetDirectory.exists()) {
                throw new RuntimeException("find: path does not exist " + args.get(0));
            }
            patternArg = args.get(2);
        }

        patternArg = patternArg.replace("'", "");

        List<String> allReturnPaths = findPattern(patternArg, targetDirectory);

        for (String path : allReturnPaths) {
            output.write(path);
            output.write(System.getProperty("line.separator"));
            output.flush();
        }

    }
}
