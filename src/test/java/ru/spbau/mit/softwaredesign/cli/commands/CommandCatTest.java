package ru.spbau.mit.softwaredesign.cli.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.softwaredesign.cli.pipe.InputBuffer;
import ru.spbau.mit.softwaredesign.cli.pipe.OutputBuffer;
import ru.spbau.mit.softwaredesign.cli.utils.BoundVariablesStorage;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class CommandCatTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private CommandCat commandCat;
    private String testData;

    @Before
    public void setUp() {
        testData = "hello world" + System.getProperty("line.separator") + "i'm alive" + System.getProperty("line.separator");
        commandCat = new CommandCat();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
        OutputBuffer.redirectToInput();
        InputBuffer.flush();
    }

    @Test
    public void single_cat_without_arguments_prints_user_input() {
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(testData.getBytes()));
            commandCat.executeCatWithUserInput();
            OutputBuffer.print();
            assertEquals(testData, outContent.toString());
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.setIn(stdin);
        }
    }

    @Test
    public void single_cat_with_arguments_reads_file_and_prints_it_to_output() {
        try {
            String curDir = BoundVariablesStorage.getCurrentPath();
            String filename = curDir + "/" + "cattest.txt";

            PrintWriter out = new PrintWriter(filename);
            out.write(testData);
            out.close();

            commandCat.execute(Collections.singletonList(filename));
            OutputBuffer.print();
            assertEquals(testData, outContent.toString());
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void single_cat_with_arguments_reads_many_files_and_prints_it_to_output() {
        try {
            String filename = "cattest.txt";
            PrintWriter out = new PrintWriter(filename);
            out.write(testData);
            out.close();

            String filename2 = "cattest2.txt";
            out = new PrintWriter(filename2);
            out.write(testData);
            out.close();

            commandCat.execute(Arrays.asList(filename, filename2));
            OutputBuffer.print();
            assertEquals(testData + testData, outContent.toString());
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void single_cat_with_arguments_notifies_about_absence_of_file() {
        try {
            String curDir = BoundVariablesStorage.getCurrentPath();
            String filename = curDir + "/" + "cattest.txt";

            PrintWriter out = new PrintWriter(filename);
            out.write(testData);
            out.close();

            String filename2 = curDir + "/" + "cattest3.txt";

            commandCat.execute(Arrays.asList(filename, filename2));
            OutputBuffer.print();
            assertEquals("cat: " + filename2 + ": error - file not found" +
                    System.getProperty("line.separator") + testData, outContent.toString());
            outContent.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
