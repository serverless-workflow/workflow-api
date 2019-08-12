package org.servlerless.workflow.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import org.hamcrest.*;
import org.skyscreamer.jsonassert.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IsEqualJSON extends DiagnosingMatcher<Object> {

    private final String expectedJSON;
    private JSONCompareMode jsonCompareMode;

    public IsEqualJSON(final String expectedJSON) {
        this.expectedJSON = expectedJSON;
        this.jsonCompareMode = JSONCompareMode.STRICT;
    }

    public IsEqualJSON leniently() {
        jsonCompareMode = JSONCompareMode.LENIENT;
        return this;
    }

    public void describeTo(final Description description) {
        description.appendText(expectedJSON);
    }

    @Override
    protected boolean matches(final Object actual, final Description mismatchDescription) {
        final String actualJSON = toJSONString(actual);
        try {
            final JSONCompareResult result = JSONCompare.compareJSON(expectedJSON, actualJSON, jsonCompareMode);
            if (!result.passed()) {
                mismatchDescription.appendText(result.getMessage());
            }
            return result.passed();
        } catch (Exception e) {
            return false;
        }
    }

    private static String toJSONString(final Object o) {
        try {
            return o instanceof String ? (String) o : new ObjectMapper().writeValueAsString(o);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFileContents(final Path path) {
        try {
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Factory
    public static IsEqualJSON equalToJSON(final String expectedJSON) {
        return new IsEqualJSON(expectedJSON);
    }

    @Factory
    public static IsEqualJSON equalToJSONInFile(final Path expectedPath) {
        return equalToJSON(getFileContents(expectedPath));
    }

    @Factory
    public static IsEqualJSON equalToJSONInFile(final String expectedFileName) {
        return equalToJSONInFile(Paths.get(expectedFileName));
    }
}