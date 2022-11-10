package hu.webarticum.treeprinter.text;

import java.util.regex.Pattern;
import java.text.Normalizer;

public class TextUtil {

    private static final String ANSI_RESET = "\u001B[0m";

    private static final Pattern LINE_SEPARATOR_PATTERN = Pattern.compile("\\R");
    
    private static final Pattern ANSI_ESCAPE_PATTERN = Pattern.compile("\\e\\[[0-9;]*m");

    private static final Pattern ASCII_CONTROL_PATTERN = Pattern.compile("[\\u0000-\\u0008\\u000B\\u000C\\u000E-\\u001F]");

    private static final Pattern ASCII_CONTROL_EXCEPT_FORMATTING_ESC_PATTERN =
            Pattern.compile("([\\u0000-\\u0008\\u000B\\u000C\\u000E-\\u001A\\u001C-\\u001F]|\\e(?!\\[[0-9;]*m))");

    private static final String TAB_SPACES = "    ";
    
    
    private TextUtil() {
        // utility class
    }

    public static String[] linesOf(String text) {
        return LINE_SEPARATOR_PATTERN.split(text);
    }

    public static ConsoleText[] linesOf(ConsoleText text) {
        boolean isPlain = (text instanceof PlainConsoleText);
        String stringContent = isPlain ? text.plain() : text.ansi();
        String[] lines = linesOf(stringContent);
        ConsoleText[] result = new ConsoleText[lines.length];
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            result[i] = isPlain ? ConsoleText.of(line) : ConsoleText.ofAnsi(line);
        }
        return result;
    }
    
    public static String ansiReset() {
        return ANSI_RESET;
    }
    
    public static String ansiToPlain(String ansiText) {
        String generatedPlainText = ANSI_ESCAPE_PATTERN.matcher(ansiText).replaceAll("");
        return generatedPlainText.equals(ansiText) ? ansiText : generatedPlainText;
    }
    
    public static String cleanPlain(String rawPlainText) {
        return clean(rawPlainText, ASCII_CONTROL_PATTERN);
    }
    
    public static String cleanAnsi(String rawAnsiText) {
        return clean(rawAnsiText, ASCII_CONTROL_EXCEPT_FORMATTING_ESC_PATTERN);
    }

    private static String clean(String rawText, Pattern controlPattern) {
        String cleanText = controlPattern.matcher(rawText).replaceAll("");
        cleanText = Normalizer.normalize(cleanText, Normalizer.Form.NFD);
        cleanText = cleanText.replace("\t", TAB_SPACES);
        // TODO: normalize newlines?
        return cleanText.equals(rawText) ? rawText : cleanText;
    }

    public static String repeat(char character, int repeats) {
        StringBuilder resultBuilder = new StringBuilder();
        repeat(resultBuilder, character, repeats);
        return resultBuilder.toString();
    }
    
    public static void repeat(StringBuilder stringBuilder, char character, int repeats) {
        for (int i = 0; i < repeats; i ++) {
            stringBuilder.append(character);
        }
    }

}
