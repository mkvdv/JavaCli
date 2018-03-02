package ru.spbau.mit.softwaredesign.cli.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.softwaredesign.cli.errors.ExpectedExitException;
import ru.spbau.mit.softwaredesign.cli.errors.PipelineException;
import ru.spbau.mit.softwaredesign.cli.errors.UnknownExternalCommandException;
import ru.spbau.mit.softwaredesign.cli.pipe.InputBuffer;
import ru.spbau.mit.softwaredesign.cli.pipe.OutputBuffer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ChainExecutorTest {

    private ChainExecutor executor;
    private String testData;
    private String expectedWcData;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        testData = "hello world" + System.getProperty("line.separator") + "i'm alive" + System.getProperty("line.separator");
        expectedWcData = "\t2\t4\t22";
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
        OutputBuffer.redirectToInput();
        InputBuffer.flush();
    }

    @Test
    public void chaining_separates_tokens_list_to_blocks_by_pipe_symbol() {
        List<String> tokens = Arrays.asList("1", " ", " ", "|", "2", "|", " ", "3", "|", " ", "4");
        executor = new ChainExecutor(tokens);
        List<List<String>> blocks = executor.getTokenizedCommandChain();
        assertEquals(blocks.size(), 4);
        assertEquals(blocks.get(0).size(), 3);
        assertEquals(blocks.get(1).size(), 1);
        assertEquals(blocks.get(2).size(), 2);
        assertEquals(blocks.get(3).size(), 2);
    }

    @Test(expected = PipelineException.class)
    public void if_two_pipes_in_a_row_then_there_must_be_thrown_an_exception() throws UnknownExternalCommandException, PipelineException, ExpectedExitException {
        List<String> tokens = Arrays.asList("pwd", " ", " ", "|", "|", "|", " ", "3", "|", " ", "4");
        executor = new ChainExecutor(tokens);
        executor.execute();
    }

    @Test
    public void cat_that_is_not_in_the_first_place_in_the_pipeline_redirects_input_to_output() throws UnknownExternalCommandException, PipelineException, ExpectedExitException {
        String filename = "cattest.txt";
        List<String> tokens = Arrays.asList("wc", " ", filename, " ", "|", " ", "cat");
        executor = new ChainExecutor(tokens);
        executor.execute();
        OutputBuffer.print();
        assertEquals(outContent.toString(), expectedWcData + " " + filename + System.getProperty("line.separator"));
    }

    @Test
    public void wc_that_is_not_in_the_first_place_in_the_pipeline_counts_from_input_buffer() throws UnknownExternalCommandException, PipelineException, ExpectedExitException {
        String filename = "cattest.txt";
        List<String> tokens = Arrays.asList("cat", " ", filename, " ", "|", " ", "wc");
        executor = new ChainExecutor(tokens);
        executor.execute();
        OutputBuffer.print();
        assertEquals(outContent.toString(), expectedWcData + System.getProperty("line.separator"));
    }

    @Test
    public void echo_that_is_not_in_the_first_place_in_the_pipeline_returns_its_argument() throws UnknownExternalCommandException, PipelineException, ExpectedExitException {
        String filename = "cattest.txt";
        List<String> tokens = Arrays.asList("wc", " ", filename, " ", "|", " ", "echo", "x");
        executor = new ChainExecutor(tokens);
        executor.execute();
        OutputBuffer.print();
        assertEquals(outContent.toString(), "x" + System.getProperty("line.separator"));
    }
}
