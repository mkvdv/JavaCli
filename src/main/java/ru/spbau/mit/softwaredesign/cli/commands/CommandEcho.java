package ru.spbau.mit.softwaredesign.cli.commands;

import ru.spbau.mit.softwaredesign.cli.pipe.InputBuffer;
import ru.spbau.mit.softwaredesign.cli.pipe.OutputBuffer;
import ru.spbau.mit.softwaredesign.cli.utils.TokenizerHelper;

import java.util.List;
import java.util.StringJoiner;

/**
 * Redefined "echo" command.
 */
public class CommandEcho implements AbstractCommand {

    public CommandEcho() {}

    /**
     * Implements "echo" function without parameters.
     * Produces empty string.
     */
    @Override
    public void execute() {
        OutputBuffer.add(System.getProperty("line.separator"));
    }

    /**
     * Implements "echo" function with parameters.
     * Interprets all arguments as one string, but ignores extra whitespaces.
     *
     * @param args Arguments of command
     */
    @Override
    public void execute(List<String> args) {
        StringJoiner joiner = new StringJoiner(" ");
        for (String nextArgument: args) {
            if (!nextArgument.equals(TokenizerHelper.whitespace))
                joiner.add(nextArgument);
        }
        OutputBuffer.add(joiner.toString());
        OutputBuffer.add(System.getProperty("line.separator"));
    }
}
