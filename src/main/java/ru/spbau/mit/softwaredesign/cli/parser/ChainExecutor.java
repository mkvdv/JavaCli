package ru.spbau.mit.softwaredesign.cli.parser;

import ru.spbau.mit.softwaredesign.cli.errors.ErrorMessage;
import ru.spbau.mit.softwaredesign.cli.errors.ExpectedExitException;
import ru.spbau.mit.softwaredesign.cli.errors.PipelineException;
import ru.spbau.mit.softwaredesign.cli.errors.UnknownExternalCommandException;
import ru.spbau.mit.softwaredesign.cli.pipe.BlockCounter;
import ru.spbau.mit.softwaredesign.cli.pipe.InputBuffer;
import ru.spbau.mit.softwaredesign.cli.pipe.OutputBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that transforms tokens stream to blocks and executes these blocks consequently.
 * "Block" means part of the pipeline (e.g. tokens sublist separated by pipeline)
 * or the whole tokens list if pipeline is not used in the command line prompt.
 */
public class ChainExecutor {

    private List<List<String>> tokenizedCommandChain;

    /**
     * Construct chain of blocks.
     * @param tokens input tokens list
     */
    public ChainExecutor(List<String> tokens) {
        BlockCounter.reset();
        if (tokens.size() == 0)
            return;

        tokenizedCommandChain = new ArrayList<>();

        makeChain(tokens);
        BlockCounter.setMaxValue(tokenizedCommandChain.size());
    }

    /**
     * Execute blocks, transfer buffer data from the previous block to the next one.
     */
    public void execute() throws PipelineException, ExpectedExitException, UnknownExternalCommandException {
        BlockExecutor blockExecutor = new BlockExecutor();
        for (List<String> nextBlock : tokenizedCommandChain) {
            OutputBuffer.redirectToInput();
            if (nextBlock.size() == 0) {
                throw new PipelineException();
            }
            blockExecutor.execute(nextBlock);
            InputBuffer.flush();
            BlockCounter.increase();
        }
        OutputBuffer.print();
    }

    /**
     * Explicitly get blocks. Need for testing.
     * @return list of blocks
     */
    public List<List<String>> getTokenizedCommandChain() {
        return tokenizedCommandChain;
    }

    /**
     * Split tokens list to blocks by pipeline symbol.
     * @param tokens input tokens list
     */
    private void makeChain(List<String> tokens) {
        List<String> currentBlock = new ArrayList<>();
        for (String token : tokens) {
            if (token.equals("|")) {
                tokenizedCommandChain.add(currentBlock);
                currentBlock = new ArrayList<>();
            } else {
                currentBlock.add(token);
            }
        }

        if (!currentBlock.isEmpty()) {
            tokenizedCommandChain.add(currentBlock);
        }
    }
}
