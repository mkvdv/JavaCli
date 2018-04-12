package ru.spbau.mit.softwaredesign.cli.utils;

import ru.spbau.mit.softwaredesign.cli.errors.WrongArgumentException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Holds all variables that have been created during current session.
 */
public class BoundVariablesStorage {

    /**
     * One map entry describes a variable name and the latest value that was assigned to it.
     */
    private static Map<String, String> boundVariables = new HashMap<>();
    private static File currentWorkingPath = new File(System.getProperty("user.dir"));

    /**
     * Puts variable and its latest value to the map.
     *
     * @param variableName  The name of the variable (left side of assignment)
     * @param variableValue The value of the variable (right side of assignment)
     */
    public static void putVariable(String variableName, String variableValue) {
        boundVariables.put(variableName, variableValue);
    }

    /**
     * Tries to get the latest value assigned to variable.
     *
     * @param variableName The name of the variable (left side of assignment)
     * @return The latest value assigned to variable, or Empty, if variable name does not exist
     * (if there has not any value been assigned to variable with given name)
     */
    public static Optional<String> tryFetch(String variableName) {
        if (boundVariables.containsKey(variableName))
            return Optional.of(boundVariables.get(variableName));
        return Optional.empty();
    }

    /**
     * Return current working directory ABSOLUTE path - used everywhere after cd_ls ?
     */
    public static String getCurrentPath() {
        return currentWorkingPath.getAbsolutePath();
    }

    /**
     * What cd command in unix do
     *
     * @throws WrongArgumentException if path to folder is path to not-directory
     */
    public static void setCurrentPath(String newRelativePath) throws WrongArgumentException {
        File folderFile = new File(newRelativePath);

        if (folderFile.isAbsolute()) {
            if (!folderFile.isDirectory()) {
                throw new WrongArgumentException();
            }
            currentWorkingPath = folderFile;
            return;
        }

        try {
            folderFile = new File(getCurrentPath() + '/' + newRelativePath).getCanonicalFile();
        } catch (IOException e) {
            throw new WrongArgumentException(); // shit happens
        }
        if (!folderFile.isDirectory()) {
            throw new WrongArgumentException();
        }
        currentWorkingPath = folderFile;
    }

    /**
     * @return string representation of absolute path of this directory.
     * @throws WrongArgumentException if path to folder is path to not-directory
     */
    public static String getAbsolutePathOfFolder(String folderName) throws WrongArgumentException {
        File folderFile = new File(folderName);

        if (folderFile.isAbsolute()) {
            if (!folderFile.isDirectory()) {
                throw new WrongArgumentException();
            }
            return folderFile.getAbsolutePath();
        }

        folderFile = new File(getCurrentPath() + '/' + folderName);
        if (!folderFile.isDirectory()) {
            throw new WrongArgumentException();
        }
        return folderFile.getAbsolutePath();
    }

    /**
     * @return proper absolut file path relatively of current working dir.
     * @throws WrongArgumentException if this is nonvalid filepath.
     */
    public static File getCorrectAbsolutePathOfFile(String filepath) throws WrongArgumentException {
        File file = new File(filepath);

        if (file.isAbsolute()) {
            if (!file.isFile()) {
                throw new WrongArgumentException();
            }
            return file;
        }

        file = new File(getCurrentPath() + '/' + filepath);
        if (!file.isFile()) {
            throw new WrongArgumentException();
        }
        return file;
    }
}
