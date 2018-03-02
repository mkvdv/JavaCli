package ru.spbau.mit.softwaredesign.cli.errors;

/**
 * Questioned command does not exist neither among redefined commands not among system commands.
 */
public class UnknownExternalCommandException extends CliException {

    private String from = null;

    public UnknownExternalCommandException() {}

    public UnknownExternalCommandException(String s) {
        from = s;
    }

    public String getFrom() {
        return from;
    }
}
