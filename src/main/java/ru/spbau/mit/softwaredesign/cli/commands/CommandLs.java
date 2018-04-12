package ru.spbau.mit.softwaredesign.cli.commands;

import ru.spbau.mit.softwaredesign.cli.errors.ErrorMessage;
import ru.spbau.mit.softwaredesign.cli.errors.WrongArgumentException;
import ru.spbau.mit.softwaredesign.cli.pipe.OutputBuffer;
import ru.spbau.mit.softwaredesign.cli.utils.BoundVariablesStorage;

import java.io.File;
import java.util.List;

/**
 * Redefined "cd" command.
 */
public class CommandLs implements AbstractCommand {

    public CommandLs() {
    }

    /**
     * Implements "ls" function without parameters.
     * - list current dir.
     */
    @Override
    public void execute() {
        String folderAbsPath = BoundVariablesStorage.getCurrentPath();
        executeWithFolder(folderAbsPath);
    }

    /**
     * Implements "ls" function with parameter.
     * Argument have already been interpolated, so that is file name.
     *
     * @param filenames File that "cat" must transfer to output.
     */
    @Override
    public void execute(List<String> filenames) {
        if (filenames.size() > 1) {
            ErrorMessage.print(ErrorMessage.SYNTAX_ERROR, "ls: can't have more than 1 arg");
            return;
        }

        String folderName = filenames.get(0);
        String absFolderName = null;
        try {
            absFolderName = BoundVariablesStorage.getAbsolutePathOfFolder(folderName);
        } catch (WrongArgumentException e) {
            ErrorMessage.print(ErrorMessage.SYNTAX_ERROR, "ls: no such folder " + folderName);
            return;
        }
        executeWithFolder(absFolderName);
    }

    /**
     * Helper function - do file listing rilly.
     *
     * @param folderAbsPath list folder with this name.
     */
    private void executeWithFolder(String folderAbsPath) {
        File curFolder = new File(folderAbsPath);
        File[] files = curFolder.listFiles();

        if (files == null) {
            ErrorMessage.print(ErrorMessage.IO_ERROR, "ls: ");
            return;
        }

        for (File f : files) {
            OutputBuffer.add(f.getName());
            OutputBuffer.add(System.getProperty("line.separator"));
        }
    }
}

