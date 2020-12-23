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

    public ArrayList<String> globbing(String rawCommand, File targetDirectory) throws IOException {
        String spaceRegex = "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'";
        ArrayList<String> tokens = new ArrayList<String>();
        Pattern regex = Pattern.compile(spaceRegex);
        Matcher regexMatcher = regex.matcher(rawCommand);
        String nonQuote;
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null || regexMatcher.group(2) != null) {
                String quoted = regexMatcher.group(0).trim();
                tokens.add(quoted.substring(1, quoted.length() - 1));
            } else {
                nonQuote = regexMatcher.group().trim();
                ArrayList<String> globbingResult = new ArrayList<String>();
                Path dir = targetDirectory.toPath();
                DirectoryStream<Path> stream = Files.newDirectoryStream(dir, nonQuote);
                for (Path entry : stream) {
                    globbingResult.add(entry.getFileName().toString());
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
        } else if (args.size() == 2 && (!args.get(0).equals("-name"))) {
            throw new RuntimeException("find: wrong argument " + args.get(0));
        } else if ((args.size() == 3 && (!args.get(1).equals("-name")))) {
            throw new RuntimeException("find: wrong argument " + args.get(1));
        }

        File targetDirectory;
        String patternArg = "";
        ArrayList<String> globbedPatternArgs = new ArrayList<String>();

        if (args.size() == 2) {
            targetDirectory = new File(".");
            patternArg = args.get(1);
        } else {
            String pathArg = args.get(0);
            targetDirectory = new File(pathArg);
            if (!targetDirectory.exists()) {
                throw new RuntimeException("find: path does not exist " + args.get(0));
            }
            patternArg = args.get(2);
        }

        patternArg.replace("'", "");
        globbedPatternArgs = globbing(patternArg, targetDirectory);

        ArrayList<ArrayList<String>> allReturnPaths = new ArrayList<ArrayList<String>>();

        for (String globPatternArg : globbedPatternArgs) {
            ArrayList<String> emptyPath = new ArrayList<>();
            allReturnPaths.add(findFile(targetDirectory, globPatternArg, "", emptyPath));
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
