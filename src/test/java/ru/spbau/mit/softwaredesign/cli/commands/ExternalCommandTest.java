package ru.spbau.mit.softwaredesign.cli.commands;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.softwaredesign.cli.errors.UnknownExternalCommandException;

public class ExternalCommandTest {

    private String notExistingCommand;

    @Before
    public void setUp() {
        notExistingCommand = "sys -reset--hard";
    }

    @Test(expected = UnknownExternalCommandException.class)
    public void unknown_command_invocation_must_throw_an_exception() throws UnknownExternalCommandException {
        ExternalCommand.execute(notExistingCommand);
    }
}
