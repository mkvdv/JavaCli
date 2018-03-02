package ru.spbau.mit.softwaredesign.cli.parser;

import ru.spbau.mit.softwaredesign.cli.utils.TokenizerHelper;

import java.util.List;

/**
 * Class that provides one method for splitting command line prompt to tokens.
 */
public class InputLineTokenizer {

    /**
     * Parses and splits input string into tokens.
     * @param inputLine Line that has been passed from command line
     * @return List of tokens
     */
    public static List<String> tokenize(String inputLine) {
        String[] delimiters = {TokenizerHelper.pipeSymbol,
                               TokenizerHelper.assignmentOperator,
                               TokenizerHelper.whitespace,
                               TokenizerHelper.variableReference,
                               TokenizerHelper.singleQuote,
                               TokenizerHelper.doubleQuote};
        return TokenizerHelper.tokenizeAndKeepDelimiters(inputLine, delimiters);
    }
}
