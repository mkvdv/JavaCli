package ru.spbau.mit.softwaredesign.cli.pipe;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class OutputBufferTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        OutputBuffer.redirectToInput();
        InputBuffer.flush();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
        OutputBuffer.redirectToInput();
        InputBuffer.flush();
    }

    @Test
    public void add_operation_appends_new_string_to_the_buffer() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            OutputBuffer.add(String.valueOf(i));
            sb.append(String.valueOf(i));
        }
        OutputBuffer.print();
        assertEquals(outContent.toString(), sb.toString());
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void redirecting_to_input_moves_all_data() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            OutputBuffer.add(String.valueOf(i));
            sb.append(String.valueOf(i));
        }
        OutputBuffer.redirectToInput();
        OutputBuffer.print();
        assertEquals(outContent.toString(), "");
        assertEquals(InputBuffer.get(), sb.toString());
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
