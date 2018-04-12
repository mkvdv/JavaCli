package ru.spbau.mit.softwaredesign.cli.commands;

import ru.spbau.mit.softwaredesign.cli.pipe.OutputBuffer;
import ru.spbau.mit.softwaredesign.cli.utils.BoundVariablesStorage;

import java.util.List;

/**
 * Redefined "pwd" command.
 */
public class CommandPwd implements AbstractCommand {

    public CommandPwd() {
    }

    /**
     * Implements "pwd" function without parameters.
     * Produces the path to current directory
     */
    @Override
    public void execute() {
        OutputBuffer.add(BoundVariablesStorage.getCurrentPath());
        OutputBuffer.add(System.getProperty("line.separator"));
    }

    /**
     * Implements "pwd" function with parameters.
     * Ignores all arguments and just produces the path to current directory
     * (as well as the same method without arguments).
     *
     * @param args Useless arguments of command
     */
    @Override
    public void execute(List<String> args) {
        execute();
    }
}
