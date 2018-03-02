package ru.spbau.mit.softwaredesign.cli.pipe;

import java.util.stream.Stream;

/**
 * Buffer with data that is produced by commands executions.
 */
public class OutputBuffer {

    private static StringBuilder output = new StringBuilder();

    /**
     * Add new record to the buffer.
     * @param s record for adding
     */
    public static void add(String s) {
        output.append(s);
    }

    /**
     * Redirect all data inside the buffer to the input.
     * It is needed when the next command in the pipeline wants to read what the previous command produced.
     */
    public static void redirectToInput() {
        InputBuffer.add(output.toString());
        flush();
    }

    /**
     * Redirect all data to the output stream.
     */
    public static void print() {
        Stream.of(output).forEach(System.out::print);
        flush();
    }

    /**
     * Clear buffer.
     */
    private static void flush() {
        output.setLength(0);
    }
}
