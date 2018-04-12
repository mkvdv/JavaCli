package ru.spbau.mit.softwaredesign.cli.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.softwaredesign.cli.pipe.InputBuffer;
import ru.spbau.mit.softwaredesign.cli.pipe.OutputBuffer;
import ru.spbau.mit.softwaredesign.cli.repl.Cli;
import ru.spbau.mit.softwaredesign.cli.utils.BoundVariablesStorage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CommandLsTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final String base = BoundVariablesStorage.getCurrentPath();
    private final String NL = System.getProperty("line.separator");
    private final String expected = "cattest.txt" + NL +
            "cattest2.txt" + NL +
            "res" + NL +
            "java" + NL;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
        OutputBuffer.redirectToInput();
        InputBuffer.flush();
    }

    @Test
    public void ls_without_arguments() {
        try {
            Cli.execute("cd src/test");
            Cli.execute("ls");
            assertEquals(expected, outContent.toString());
            Cli.execute("cd " + base); // back ! or tests will be broken

            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void ls_with_directory_argument() {
        try {
            Cli.execute("ls src/test");
            assertEquals(expected, outContent.toString());

            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
