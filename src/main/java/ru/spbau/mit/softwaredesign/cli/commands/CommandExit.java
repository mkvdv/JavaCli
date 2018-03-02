package ru.spbau.mit.softwaredesign.cli.commands;

import ru.spbau.mit.softwaredesign.cli.errors.ExpectedExitException;
import ru.spbau.mit.softwaredesign.cli.pipe.OutputBuffer;

import java.util.List;

/**
 * Redefined "exit" command.
 */
public class CommandExit implements AbstractCommand {

    public CommandExit() {}

    /**
     * Implements "exit" function without parameters.
     * Shutdowns the program.
     *
     * @throws ExpectedExitException as signal of normal exiting
     */
    @Override
    public void execute() throws ExpectedExitException {
        OutputBuffer.add("bye. and don't write to me anymore...");
        OutputBuffer.add(System.getProperty("line.separator"));
        OutputBuffer.print();
        throw new ExpectedExitException();
    }

    /**
     * Implements "exit" function with parameters.
     * Ignores all arguments and just shutdowns the program
     * (as well as the same method without arguments).
     *
     * @param args Useless arguments of command
     *
     * @throws ExpectedExitException as signal of normal exiting
     */
    @Override
    public void execute(List<String> args) throws ExpectedExitException {
        execute();
    }
}
