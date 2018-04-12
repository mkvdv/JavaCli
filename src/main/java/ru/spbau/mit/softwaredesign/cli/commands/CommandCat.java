package ru.spbau.mit.softwaredesign.cli.commands;

import ru.spbau.mit.softwaredesign.cli.errors.ErrorMessage;
import ru.spbau.mit.softwaredesign.cli.errors.WrongArgumentException;
import ru.spbau.mit.softwaredesign.cli.pipe.BlockCounter;
import ru.spbau.mit.softwaredesign.cli.pipe.InputBuffer;
import ru.spbau.mit.softwaredesign.cli.pipe.OutputBuffer;
import ru.spbau.mit.softwaredesign.cli.utils.BoundVariablesStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Redefined "cat" command.
 */
public class CommandCat implements AbstractCommand {

    public CommandCat() {
    }

    /**
     * Implements "cat" function without parameters.
     * Behavior depends of position of this command in the pipeline:
     * - if this is the first command in the pipeline, then repeat the following user input.
     * - otherwise redirect data from input buffer to output buffer.
     *
     * @throws IOException for the case of input/output error
     */
    @Override
    public void execute() {
        if (BlockCounter.get() == 0) {
            try {
                executeCatWithUserInput();
            } catch (IOException e) {
                ErrorMessage.print(ErrorMessage.IO_ERROR, "cat: ");
            }
        } else {
            executeCatWithInputBuffer();
        }
    }

    /**
     * Implements "cat" function with parameters.
     * Arguments have already been interpolated, so that is file names.
     *
     * @param filenames Files that "cat" must transfer to output.
     */
    @Override
    public void execute(List<String> filenames) {
        Charset charset = Charset.forName("UTF-8");
        for (String filename : filenames) {
            try {
                filename = BoundVariablesStorage.getCorrectAbsolutePathOfFile(filename).getAbsolutePath();
            } catch (WrongArgumentException e) {
                ErrorMessage.print(ErrorMessage.FILE_NOT_FOUND, "cat: " + filename);
                continue;
            }

            try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename), charset)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    OutputBuffer.add(line);
                    OutputBuffer.add(System.getProperty("line.separator"));
                }
            } catch (IOException e) {
                ErrorMessage.print(ErrorMessage.FILE_NOT_FOUND, "cat: " + filename);
            }
        }
    }

    /**
     * Implements behavior when no arguments were passed and this is the first command in pipeline.
     * Simply redirects console input to output.
     * <p>
     * If it is the last block, print every line immediately.
     *
     * @throws IOException for the case of input/output error
     */
    public void executeCatWithUserInput() throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            final String nextLine = br.readLine();
            if (nextLine == null || nextLine.length() == 0) {
                br.close();
                return;
            }
            OutputBuffer.add(nextLine);
            OutputBuffer.add(System.getProperty("line.separator"));
            if (BlockCounter.hasReachedMaximum()) {
                OutputBuffer.print();
            }
        }
    }

    /**
     * Implements behavior when no arguments were passed and this is not the first command in pipeline.
     * Simply redirects input buffer to output buffer.
     */
    private void executeCatWithInputBuffer() {
        OutputBuffer.add(InputBuffer.get());
    }
}
