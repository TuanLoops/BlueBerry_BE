package com.blueberry.util;

public class StringTrimmer {
    public static String trim(String input) {

        // Remove leading and trailing spaces
        String trimmed = input.trim();

        // Replace multiple line breaks with a single line break
        trimmed = trimmed.replaceAll("(\n)+", "\n");

        return trimmed;
    }
}
