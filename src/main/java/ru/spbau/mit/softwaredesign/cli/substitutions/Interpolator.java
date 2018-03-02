package ru.spbau.mit.softwaredesign.cli.substitutions;

import ru.spbau.mit.softwaredesign.cli.utils.BoundVariablesStorage;
import ru.spbau.mit.softwaredesign.cli.errors.ErrorMessage;
import ru.spbau.mit.softwaredesign.cli.errors.UncompletedLineException;
import ru.spbau.mit.softwaredesign.cli.parser.InputLineTokenizer;
import ru.spbau.mit.softwaredesign.cli.utils.TokenizerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class used for interpolating command line prompt.
 */
public class Interpolator {

    private List<String> interpolatedTokensList;

    public Interpolator() {
        interpolatedTokensList = new ArrayList<>();
    }

    /**
     * Interpolates input string.
     * Substitutions are applying to:
     *  - expressions inside single quotes,
     *  - expressions inside double quotes,
     *  - variable references.
     *
     * @param tokensList List of tokens representing input string
     * @return List of tokens after all possible substitutions
     */
    public List<String> interpolate(List<String> tokensList) throws UncompletedLineException {
        int currentTokenPosition = 0;

        while (currentTokenPosition < tokensList.size()) {
            String currentToken = tokensList.get(currentTokenPosition);
            if (currentToken.matches(TokenizerHelper.singleQuote)) {
                currentTokenPosition = replaceOnSingleQuotes(tokensList, currentTokenPosition) + 1;
            } else if (currentToken.matches(TokenizerHelper.doubleQuote)) {
                currentTokenPosition = replaceOnDoubleQuotes(tokensList, currentTokenPosition) + 1;
            } else if (currentToken.matches(TokenizerHelper.variableReference)) {
                interpolatedTokensList.addAll(InputLineTokenizer.tokenize(expandProbableVariableReference(tokensList, currentTokenPosition)));
                currentTokenPosition += 2;
            } else {
                interpolatedTokensList.add(tokensList.get(currentTokenPosition));
                currentTokenPosition++;
            }
        }
        return interpolatedTokensList;
    }

    /**
     * Interpolates all data between single quotes, interprets that data as a single raw string.
     * @param tokensList List of tokens
     * @param firstQuotePosition Position of left quote
     * @return Position of right quote
     * @throws UncompletedLineException if right quote does not exist
     */
    private int replaceOnSingleQuotes(List<String> tokensList, int firstQuotePosition) throws UncompletedLineException {
        StringBuilder rawStringBuilder = new StringBuilder();
        int i = firstQuotePosition + 1;
        while (i < tokensList.size() && !tokensList.get(i).matches(TokenizerHelper.singleQuote)) {
            rawStringBuilder.append(tokensList.get(i));
            i++;
        }
        if (i == tokensList.size())
            throw new UncompletedLineException();
        interpolatedTokensList.add(rawStringBuilder.toString());
        return i;
    }

    /**
     * Interpolates all data between double quotes, interprets that data as a single raw string.
     * Sign "$" with the following non-whitespace symbol is treated as variable reference.
     *
     * @param tokensList List of tokens
     * @param firstQuotePosition Position of left quote
     * @return Position of right quote
     * @throws UncompletedLineException if right quote does not exist
     */
    private int replaceOnDoubleQuotes(List<String> tokensList, int firstQuotePosition) throws UncompletedLineException {
        StringBuilder rawStringBuilder = new StringBuilder();
        int i = firstQuotePosition + 1;
        while (i < tokensList.size() && !tokensList.get(i).matches(TokenizerHelper.doubleQuote)) {
            if (tokensList.get(i).matches(TokenizerHelper.variableReference)) {
                InputLineTokenizer.tokenize(expandProbableVariableReference(tokensList, i)).forEach(rawStringBuilder::append);
                /* skip reference sign and the following token */
                i += 2;
            } else {
                rawStringBuilder.append(tokensList.get(i));
                i++;
            }
        }
        if (i >= tokensList.size())
            throw new UncompletedLineException();
        interpolatedTokensList.add(rawStringBuilder.toString());
        return i;
    }

    /**
     * Try to substitute variable with its reference.
     * 
     * @param tokensList List of tokens
     * @param referencePosition Position of reference sign in the list
     * @return
     *  - reference sign, if the next symbol to the reference sign is whitespace
     *  - variable reference, if variable with given name exists
     *  - empty string otherwise
     */
    private String expandProbableVariableReference(List<String> tokensList, int referencePosition) {
        if (referencePosition == tokensList.size() - 1) {
            return "$";
        } else if (tokensList.get(referencePosition + 1).matches("\\s")) {
            return "$" + tokensList.get(referencePosition + 1);
        } else {
            String variableName = tokensList.get(referencePosition + 1);
            Optional<String> maybeVariableValue = BoundVariablesStorage.tryFetch(variableName);
            return maybeVariableValue.orElse("");
        }
    }
}
