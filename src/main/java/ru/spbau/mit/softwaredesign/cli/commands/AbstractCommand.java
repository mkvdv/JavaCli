package ru.spbau.mit.softwaredesign.cli.commands;

import ru.spbau.mit.softwaredesign.cli.errors.ExpectedExitException;

import java.io.IOException;
import java.util.List;

/**
 * Common contract of command that behavior is defined by the code.
 */
public interface AbstractCommand {

    /**
     * Executes command that takes no parameters.
     */
    public void execute() throws ExpectedExitException;

    /**
     * Executes command with given args.
     * @param args Arguments of command
     */
    public void execute(List<String> args) throws ExpectedExitException;
}
