package uk.ac.ucl.jsh;

import java.util.ArrayList;
import java.util.List;

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
        System.out.println(tree.toStringTree());
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (!tree.getChild(i).getText().equals(";")) {
                lastSubcommand += tree.getChild(i).getText();
            } else {
                rawCommands.add(lastSubcommand);
                lastSubcommand = "";
            }
        }
        rawCommands.add(lastSubcommand);

        return rawCommands;
    }

}
