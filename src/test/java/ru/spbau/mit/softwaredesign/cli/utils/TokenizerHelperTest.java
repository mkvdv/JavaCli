package ru.spbau.mit.softwaredesign.cli.utils;

import org.junit.Test;

import java.util.List;
import java.util.StringJoiner;

import static org.junit.Assert.assertEquals;

public class TokenizerHelperTest {

    @Test
    public void tokenizing_must_return_all_tokens_including_delimiters() {
        String[] delimiters = new String[]{"\\.", "\\,", "\\-"};
        List<String> tokens = TokenizerHelper.tokenizeAndKeepDelimiters("a,b.c--d e", delimiters);
        assertEquals(tokens.size(), 8);
        assertEquals(tokens.get(0), "a");
        assertEquals(tokens.get(1), ",");
        assertEquals(tokens.get(2), "b");
        assertEquals(tokens.get(3), ".");
        assertEquals(tokens.get(4), "c");
        assertEquals(tokens.get(5), "-");
        assertEquals(tokens.get(6), "-");
        assertEquals(tokens.get(7), "d e");
    }

    @Test
    public void tokens_represent_a_partition_of_string() {
        String[] delimiters = new String[]{"\\.", "\\,", "\\-"};
        List<String> tokens = TokenizerHelper.tokenizeAndKeepDelimiters("a,b.c--d e", delimiters);
        StringJoiner joiner = new StringJoiner("");
        tokens.forEach(joiner::add);
        assertEquals(joiner.toString(), "a,b.c--d e");
    }
}
