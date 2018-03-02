package ru.spbau.mit.softwaredesign.cli.utils;

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

    /**
     * Puts variable and its latest value to the map.
     * @param variableName The name of the variable (left side of assignment)
     * @param variableValue The value of the variable (right side of assignment)
     */
    public static void putVariable(String variableName, String variableValue) {
        boundVariables.put(variableName, variableValue);
    }

    /**
     * Tries to get the latest value assigned to variable.
     * @param variableName The name of the variable (left side of assignment)
     * @return The latest value assigned to variable, or Empty, if variable name does not exist
     * (if there has not any value been assigned to variable with given name)
     */
    public static Optional<String> tryFetch(String variableName) {
        if (boundVariables.containsKey(variableName))
            return Optional.of(boundVariables.get(variableName));
        return Optional.empty();
    }
}
