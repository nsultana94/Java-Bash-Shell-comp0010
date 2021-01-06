package uk.ac.ucl.jsh;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Uses the ANTLR parser to parse the command line
 * @author Saachi Pahwa
 * @author Naima Sultana 
 * @author Joshua Mukherjee
 */

public class JSHParser {
    
    /**
     * Gets the sub commands from the command line input
     * @param cmdline command line input
     * @return
     */
    public List<String> get_sub_commands(String cmdline) {
        CharStream parserInput = CharStreams.fromString(cmdline);
        JshGrammarLexer lexer = new JshGrammarLexer(parserInput);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        JshGrammarParser parser = new JshGrammarParser(tokenStream);
        ParseTree tree = parser.command(); 
        List<String> rawCommands = new ArrayList<>();
        String lastSubcommand = "";
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (!tree.getChild(i).getText().equals(";")) {
                lastSubcommand += tree.getChild(i).getText();
            } else {
                rawCommands.add(lastSubcommand);
                lastSubcommand = "";
            }
        }
        rawCommands.add(lastSubcommand);
        Pattern pattern = Pattern.compile("`(.*)$");
        Pattern pattern2 = Pattern.compile("(.*?)`");
        StringBuilder sb = new StringBuilder();
        
        for(int c = 0; c < rawCommands.size(); c++){
            Matcher matcher = pattern.matcher(rawCommands.get(c));
            if (matcher.find() && c != rawCommands.size() -1){
                String first = matcher.group();
                Matcher matcher2 = pattern2.matcher(rawCommands.get(c +1));
                if(matcher2.find()){
                    String second = matcher2.group();   
                    String a = rawCommands.get(c).replace(first, "");
                    String b = rawCommands.get(c + 1).replace(second, "");
                    rawCommands.remove(c);
                    rawCommands.remove(c);
                    sb.append(a);
                    sb.append(first);
                    sb.append(";");
                    sb.append(second);
                    sb.append(b);
                    
                    rawCommands.add(c, sb.toString());
                    break;

                    
                }
            }
            
            
        } 
        

        return rawCommands;
    }

}
