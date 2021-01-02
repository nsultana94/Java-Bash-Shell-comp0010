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
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

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

    public List<String> findFile(File targetDirectory, String patternArg, String returnPath,
            List<String> argReturnPaths) {
        File[] targetDirListing = targetDirectory.listFiles();
        if (targetDirListing != null) {
            for (File child : targetDirListing) {
                if (Pattern.matches(patternArg, child.getName())) {
                    argReturnPaths.add(returnPath + "/" + child.getName());
                }
                findFile(child, patternArg, returnPath + "/" + child.getName(), argReturnPaths);

            }
        }
        return argReturnPaths;
    }

    public List<String> globbing(String rawCommand, File targetDirectory) throws IOException {
        String spaceRegex = "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'";
        List<String> tokens = new ArrayList<>();
        Pattern regex = Pattern.compile(spaceRegex);
        Matcher regexMatcher = regex.matcher(rawCommand);
        String nonQuote;
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null || regexMatcher.group(2) != null) {
                String quoted = regexMatcher.group().trim();
                tokens.add(quoted.substring(1, quoted.length() - 1));
            } else {

                nonQuote = regexMatcher.group().trim();
                Path dir = targetDirectory.toPath();
                List<String> globbingResult = new ArrayList<>();
                String pattern1 = "";

                String path = nonQuote;
                if (path.lastIndexOf("/") >= 0 && path.lastIndexOf("*") >= 0
                        && path.lastIndexOf("/") < path.lastIndexOf("*")) {
                    pattern1 = path.substring(path.lastIndexOf("/") + 1);
                    path = path.substring(0, path.lastIndexOf("/"));
                    targetDirectory = new File(dir + "/" + path);

                    if (targetDirectory.exists()) {

                        dir = targetDirectory.toPath();
                        DirectoryStream<Path> stream = Files.newDirectoryStream(dir, pattern1);
                        for (Path entry : stream) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(path);
                            sb.append("/");
                            sb.append(entry.getFileName().toString());

                            globbingResult.add(sb.toString());

                        }

                    }
                }

                else {
                    DirectoryStream<Path> stream = Files.newDirectoryStream(dir, nonQuote);
                    for (Path entry : stream) {
                        globbingResult.add(entry.getFileName().toString());
                    }
                }

                if (globbingResult.isEmpty()) {
                    globbingResult.add(nonQuote);
                }
                tokens.addAll(globbingResult);
            }
        }

        return tokens;
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
        List<String> globbedPatternArgs = new ArrayList<>();

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
        globbedPatternArgs = globbing(patternArg, targetDirectory);

        List<List<String>> allReturnPaths = new ArrayList<>();

        for (String globPatternArg : globbedPatternArgs) {
            List<String> emptyPath = new ArrayList<>();
            allReturnPaths.add(findFile(targetDirectory, globPatternArg, targetDirectory.getName(), emptyPath));
        }

        for (List<String> argReturnPaths : allReturnPaths) {
            for (String path : argReturnPaths) {
                output.write(path);
                output.write(System.getProperty("line.separator"));
                output.flush();
            }
        }

    }
}
