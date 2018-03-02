package ru.spbau.mit.softwaredesign.cli.pipe;

/**
 * Counter of blocks in the pipeline.
 * Current value means position of current block in the pipeline.
 * Used when specific commands without parameters are met
 * (their behavior depends on when exactly they are).
 */
public class BlockCounter {

    private static int counter = 0;
    private static int maxValue;

    /**
     * Get current value of counter.
     * @return Value of counter
     */
    public static int get() {
        return counter;
    }

    /**
     * Increase counter value.
     */
    public static void increase() {
        counter++;
    }

    /**
     * Reset counter (between different command-line prompts)
     */
    public static void reset() {
        counter = 0;
    }

    /**
     * Get maximum possible value of counter (it is expected to be a number of blocks in the chain).
     * @return maximum possible value
     */
    public static int getMaxValue() {
        return maxValue;
    }

    /**
     * Set maximum possible value to counter (it is expected to be a number of blocks in the chain).
     * @param value maximum possible value
     */
    public static void setMaxValue(int value) {
        maxValue = value;
    }

    /**
     * Checks if the counter value has reached its maximum value.
     * @return yes/no result of checking
     */
    public static boolean hasReachedMaximum() {
        return counter == maxValue - 1;
    }
}
