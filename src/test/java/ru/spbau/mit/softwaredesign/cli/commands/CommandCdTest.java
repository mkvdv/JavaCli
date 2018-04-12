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

public class CommandCdTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final String base = BoundVariablesStorage.getCurrentPath();
    private final String NL = System.getProperty("line.separator");

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
    public void cd_empty() {
        try {
            Cli.execute("cd");
            Cli.execute("pwd");
            assertEquals(base + NL, outContent.toString());

            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void cd_cat_wc_pwd() {
        try {
            Cli.execute("cd src/test/res");

            Cli.execute("cat f1");
            Cli.execute("wc f1");
            assertEquals("111111" + NL +
                            "\t1\t1\t7 " + BoundVariablesStorage.getCurrentPath() + "/" + "f1" + NL,
                    outContent.toString());

            // pwd is ok, depends on system, not checked here

            Cli.execute("cd " + base);
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
