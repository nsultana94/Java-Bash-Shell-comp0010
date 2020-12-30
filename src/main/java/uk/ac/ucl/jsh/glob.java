package uk.ac.ucl.jsh;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Class to implement the globbing mechanism in JSH
 * Takes a string with wildcards ({@code *}) and processes it 
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class glob {

    private static CurrentDirectory currentDirectory = CurrentDirectory.getInstance();
    /**
     * Converts raw commands to an {@code ArrayList} of tokens
     * @param rawCommand the raw command to be processed
     * @return {@code ArrayList} of tokens after replacing {@code *}'s
     * @throws IOException
     */
    
    public List<String> get_tokens(String rawCommand) throws IOException {
        String spaceRegex = "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'";
        List<String> tokens = new ArrayList<>();
        Pattern regex = Pattern.compile(spaceRegex);
        Matcher regexMatcher = regex.matcher(rawCommand);
        String nonQuote;
        while (regexMatcher.find()) {
            if (regexMatcher.group(1) != null || regexMatcher.group(2) != null) {
                String quoted = regexMatcher.group(0).trim();
                tokens.add(quoted.substring(1, quoted.length() - 1));
            } else {
                nonQuote = regexMatcher.group().trim();
                List<String> globbingResult = new ArrayList<>();
                Path dir = Paths.get(currentDirectory.getCurrentDirectory());
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

    
}
