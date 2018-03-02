package ru.spbau.mit.softwaredesign.cli.commands;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.softwaredesign.cli.errors.ExpectedExitException;

import java.util.Arrays;
import java.util.List;

public class CommandExitTest {

    private CommandExit commandExit;
    private List<String> exitParameters;

    @Before
    public void setUp() {
        commandExit = new CommandExit();
        exitParameters = Arrays.asList("so", "many", "useless", "arguments", "...");
    }

    @Test(expected = ExpectedExitException.class)
    public void exit_without_parameters_must_throw_expected_exit_exception() throws ExpectedExitException {
        commandExit.execute();
    }

    @Test(expected = ExpectedExitException.class)
    public void exit_with_parameters_must_throw_expected_exit_exception() throws ExpectedExitException {
        commandExit.execute(exitParameters);
    }
}
