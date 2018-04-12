package ru.spbau.mit.softwaredesign.cli.commands;

import ru.spbau.mit.softwaredesign.cli.errors.ErrorMessage;
import ru.spbau.mit.softwaredesign.cli.errors.WrongArgumentException;
import ru.spbau.mit.softwaredesign.cli.utils.BoundVariablesStorage;

import java.util.List;

/**
 * Redefined "cd" command.
 */
public class CommandCd implements AbstractCommand {

    public CommandCd() {
    }

    /**
     * Implements "cd" function without parameters - do nothing.
     */
    @Override
    public void execute() {
    }

    /**
     * Implements "cd" function with parameter.
     * Argument have already been interpolated, so that is file name.
     *
     * @param filenames Folder name that "cd" must goto.
     */
    @Override
    public void execute(List<String> filenames) {
        if (filenames.size() > 1) {
            ErrorMessage.print(ErrorMessage.SYNTAX_ERROR, "cd: can't have more than 1 arg");
            return;
        }

        String folderName = filenames.get(0);
        try {
            BoundVariablesStorage.setCurrentPath(folderName);
        } catch (WrongArgumentException e) {
            ErrorMessage.print(ErrorMessage.SYNTAX_ERROR, "cd: wrong arg, not folder?");
        }
    }
}

