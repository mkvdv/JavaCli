package ru.spbau.mit.softwaredesign.cli.pipe;

/**
 * Buffer with input data for command.
 */
public class InputBuffer {

    private static StringBuilder input = new StringBuilder();

    /**
     * Add new record to the buffer.
     * @param s record for adding
     */
    public static void add(String s) {
        input.append(s);
    }

    /**
     * Get all data from the buffer.
     * @return data packed to string
     */
    public static String get() {
        return input.toString();
    }

    /**
     * Clear buffer.
     */
    public static void flush() {
        input.setLength(0);
    }
}
