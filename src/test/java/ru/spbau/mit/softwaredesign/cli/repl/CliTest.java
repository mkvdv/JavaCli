package ru.spbau.mit.softwaredesign.cli.repl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.softwaredesign.cli.errors.*;
import ru.spbau.mit.softwaredesign.cli.pipe.InputBuffer;
import ru.spbau.mit.softwaredesign.cli.pipe.OutputBuffer;
import ru.spbau.mit.softwaredesign.cli.utils.BoundVariablesStorage;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class CliTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private String testData;
    private String expectedWcData;
    private String expectedWcTotal;

    @Before
    public void setUp() {
        testData = "hello world" + System.getProperty("line.separator") + "i'm alive" + System.getProperty("line.separator");
        expectedWcData = "\t2\t4\t22";
        expectedWcTotal = "\t4\t8\t44";
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
        OutputBuffer.redirectToInput();
        InputBuffer.flush();
    }

    @Test
    public void assignment_x_and_the_following_reference_call_return_value_of_x() throws CliException {
        Cli.execute("x=1");
        Cli.execute("echo $x");
        assertEquals(outContent.toString(), "1" + System.getProperty("line.separator"));
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void echo_with_parameter_inside_single_quotes_prints_raw_parameter() throws CliException {
        Cli.execute("x=1");
        Cli.execute("echo 'abc$x'");
        assertEquals(outContent.toString(), "abc$x" + System.getProperty("line.separator"));
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void echo_with_parameter_inside_double_quotes_substitutes_variables() throws CliException {
        Cli.execute("x=1");
        Cli.execute("echo \"abc$x\"");
        assertEquals(outContent.toString(), "abc1" + System.getProperty("line.separator"));
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UnknownExternalCommandException.class)
    public void command_with_parameters_inside_single_quotes_must_throw_an_exception() throws CliException {
        Cli.execute("'echo 1'");
    }

    @Test(expected = UnknownExternalCommandException.class)
    public void command_with_parameters_inside_double_quotes_must_throw_an_exception() throws CliException {
        Cli.execute("\"echo 1\"");
    }

    @Test
    public void assignment_command_inside_single_quotes_to_variable_gives_correct_command_call() throws CliException, FileNotFoundException {
        String filename = "cattest.txt";
        PrintWriter out = new PrintWriter(filename);
        out.write(testData);
        out.close();

        Cli.execute("x='cat cattest.txt'");
        Cli.execute("$x");
        assertEquals(outContent.toString(), testData);
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void assignment_command_inside_double_quotes_to_variable_gives_correct_command_call() throws CliException, FileNotFoundException {
        String filename = "cattest.txt";
        PrintWriter out = new PrintWriter(filename);
        out.write(testData);
        out.close();

        Cli.execute("x=\"cat cattest.txt\"");
        Cli.execute("$x");
        assertEquals(outContent.toString(), testData);
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void many_cats_in_a_row_makes_no_effects() throws FileNotFoundException, CliException {
        String curDir = BoundVariablesStorage.getCurrentPath();
        String filename = curDir + "/" + "cattest.txt";
        PrintWriter out = new PrintWriter(filename);
        out.write(testData);
        out.close();

        String filename2 = curDir + "/" + "cattest2.txt";
        out = new PrintWriter(filename2);
        out.write(testData);
        out.close();

        Cli.execute("wc cattest.txt cattest2.txt | cat | cat | cat|cat|    cat  |  cat");
        assertEquals(expectedWcData + " " + filename + System.getProperty("line.separator")
                + expectedWcData + " " + filename2 + System.getProperty("line.separator")
                + expectedWcTotal + " total" + System.getProperty("line.separator"), outContent.toString());
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pwd_as_the_last_block_prints_only_current_directory() throws CliException, FileNotFoundException {
        String curDir = BoundVariablesStorage.getCurrentPath();
        String filename = curDir + "/" + "cattest.txt";
        PrintWriter out = new PrintWriter(filename);
        out.write(testData);
        out.close();

        String filename2 = curDir + "/" + "cattest2.txt";
        out = new PrintWriter(filename2);
        out.write(testData);
        out.close();

        Cli.execute("wc cattest.txt cattest2.txt | cat | cat | cat|cat|    cat  |  cat | pwd");
        assertEquals(outContent.toString(), curDir + System.getProperty("line.separator"));
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void quotes_inside_double_quotes_are_common_symbols() throws UncompletedLineException, ExpectedExitException, UnknownExternalCommandException, PipelineException {
        Cli.execute("echo \"'blah-blah'\"");
        assertEquals(outContent.toString(), "'blah-blah'" + System.getProperty("line.separator"));
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void quotes_inside_single_quotes_are_common_symbols() throws UncompletedLineException, ExpectedExitException, UnknownExternalCommandException, PipelineException {
        Cli.execute("echo '\"blah-blah\"'");
        assertEquals(outContent.toString(), "\"blah-blah\"" + System.getProperty("line.separator"));
        try {
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
