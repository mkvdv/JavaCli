package ru.spbau.mit.softwaredesign.cli.utils;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * Additional class that provides appropriate tokenizing methods.
 */
public class TokenizerHelper {

    public static final String pipeSymbol = "\\|";
    public static final String variableReference = "\\$";
    public static final String assignmentOperator = "=";
    public static final String whitespace = " ";
    public static final String singleQuote = "\'";
    public static final String doubleQuote = "\"";

    /**
     * Splits string by given delimiters, but with keeping them aside.
     * @param lineToBeTokenized Input string that will be tokenized
     * @param delimiters List of delimiters for tokenizing
     * @return Sequential list of tokens, including delimiters
     */
    public static List<String> tokenizeAndKeepDelimiters(String lineToBeTokenized, String[] delimiters) {
        String modifiedDelimiter = constructDelimiterRegexp(delimiters);
        return Arrays.asList(lineToBeTokenized.split(modifiedDelimiter));
    }

    /**
     * Embeds delimiters to common regular expression.
     * @param delimiters List of delimiters for tokenizing
     * @return Regular expression that will tokenize string with keeping delimiters
     */
    private static String constructDelimiterRegexp(String[] delimiters) {
        StringJoiner joiner = new StringJoiner("|");
        Stream.of(delimiters).forEach(joiner::add);
        /* Special regexp for keeping delimiters */
        String DELIMITER_KEEPER_REGEXP = "((?<=(%1$s))|(?=(%1$s)))";

        return String.format(DELIMITER_KEEPER_REGEXP, joiner.toString());
    }
}
