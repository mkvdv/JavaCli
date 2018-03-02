package ru.spbau.mit.softwaredesign.cli.substitutions;

import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.softwaredesign.cli.errors.UncompletedLineException;
import ru.spbau.mit.softwaredesign.cli.utils.BoundVariablesStorage;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import static org.junit.Assert.assertEquals;

public class InterpolatorTest {
    
    private Interpolator interpolator;

    @Before
    public void setUp() {
        interpolator = new Interpolator();
    }

    @Test
    public void interpolating_simple_expression_inside_single_quotes_returns_expression_as_string() {
        List<String> testData = Arrays.asList("'", "1", " ", "6", " ", "9", "'");
        try {
            List<String> actualResult = interpolator.interpolate(testData);
            StringJoiner joiner = new StringJoiner("");
            actualResult.forEach(joiner::add);
            assertEquals(joiner.toString(), "1 6 9");
        } catch (UncompletedLineException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UncompletedLineException.class)
    public void interpolating_simple_expression_inside_single_quotes_without_pairing_quote_must_throw_an_exception() throws UncompletedLineException {
        List<String> testData = Arrays.asList("'", "1", " ", "2", " ", "3");
        interpolator.interpolate(testData);
    }

    @Test
    public void interpolating_simple_expression_inside_double_quotes_returns_expression_as_string() {
        List<String> testData = Arrays.asList("\"", "1", " ", "4", " ", "7", "\"");
        try {
            List<String> actualResult = interpolator.interpolate(testData);
            StringJoiner joiner = new StringJoiner("");
            actualResult.forEach(joiner::add);
            assertEquals(joiner.toString(), "1 4 7");
        } catch (UncompletedLineException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = UncompletedLineException.class)
    public void interpolating_simple_expression_inside_double_quotes_without_pairing_quote_must_throw_an_exception() throws UncompletedLineException {
        List<String> testData = Arrays.asList("\"", "1", " ", "4", " ", "7");
        interpolator.interpolate(testData);
    }

    @Test
    public void expanding_existing_variable_returns_its_value() {
        BoundVariablesStorage.putVariable("x", "y");
        List<String> testData = Arrays.asList("$", "x");
        try {
            List<String> actualResult = interpolator.interpolate(testData);
            assertEquals(actualResult.size(), 1);
            assertEquals(actualResult.get(0), "y");
        } catch (UncompletedLineException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void expanding_not_existing_variable_returns_empty_string() {
        BoundVariablesStorage.putVariable("x", "y");
        List<String> testData = Arrays.asList("$", "z");
        try {
            List<String> actualResult = interpolator.interpolate(testData);
            assertEquals(actualResult.size(), 1);
            assertEquals(actualResult.get(0), "");
        } catch (UncompletedLineException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void expanding_reference_sign_without_name_returns_the_same() {
        List<String> testData = Arrays.asList("$", " ", "x");
        try {
            List<String> actualResult = interpolator.interpolate(testData);
            assertEquals(actualResult.size(), 3);
            assertEquals(actualResult.get(0), "$");
            assertEquals(actualResult.get(1), " ");
            assertEquals(actualResult.get(2), "x");
        } catch (UncompletedLineException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void interpolating_expression_with_variable_reference_inside_single_quotes_returns_expression_as_string() {
        BoundVariablesStorage.putVariable("x", "y");
        List<String> testData = Arrays.asList("'", "1", " ", "$", "x", "9", "'");
        try {
            List<String> actualResult = interpolator.interpolate(testData);
            StringJoiner joiner = new StringJoiner("");
            actualResult.forEach(joiner::add);
            assertEquals(joiner.toString(), "1 $x9");
        } catch (UncompletedLineException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void interpolating_expression_with_variable_reference_inside_double_quotes_expands_variable() {
        BoundVariablesStorage.putVariable("x", "y");
        List<String> testData = Arrays.asList("\"", "1", " ", "$", "x", "9", "\"");
        try {
            List<String> actualResult = interpolator.interpolate(testData);
            StringJoiner joiner = new StringJoiner("");
            actualResult.forEach(joiner::add);
            assertEquals(joiner.toString(), "1 y9");
        } catch (UncompletedLineException e) {
            e.printStackTrace();
        }
    }
}
