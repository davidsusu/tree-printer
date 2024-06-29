package hu.webarticum.treeprinter.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnsiLineMerger implements LineMerger {
    
    private static final char ESCAPE_CHAR = '\u001B';

    private static final Pattern ANSI_ESCAPE_START_PATTERN = Pattern.compile("^\\e(?:\\[[0-9;]*m)?");
    

    @Override
    public String merge(String existingLine, int fromPosition, String replacement) {
        StringBuilder resultBuilder = new StringBuilder();
        
        int replacementLength = replacement.length();
        StringBuilder replacementEscapeStack = new StringBuilder();
        int replacementAfterLocation = replacementLength + 1;
        int replacementDiff = locate(replacement, 0, 0, replacementAfterLocation, replacementEscapeStack);
        
        int replacementWidth = replacementAfterLocation + replacementDiff;
        int replacementVisibleEnd = fromPosition + replacementWidth;

        StringBuilder leadingEscapeStack = new StringBuilder();
        int leadingLength = -fromPosition;
        if (fromPosition > 0 && !existingLine.isEmpty()) {
            leadingLength = locate(existingLine, 0, 0, fromPosition, leadingEscapeStack);
            String leading = leadingLength > 0 ? existingLine.substring(0, leadingLength) : existingLine;
            resultBuilder.append(leading);
            if (leadingEscapeStack.length() > 0) {
                resultBuilder.append(TextUtil.ansiReset());
            }
        }
        
        if (leadingLength < 0) {
            TextUtil.repeat(resultBuilder, ' ', -leadingLength);
        }
        
        resultBuilder.append(replacement);
        if (replacementEscapeStack.length() > 0) {
            resultBuilder.append(TextUtil.ansiReset());
        }

        if (leadingLength >= 0 && !existingLine.isEmpty()) {
            StringBuilder innerEscapeStack = new StringBuilder();
            int innerRight = locate(existingLine, leadingLength, fromPosition, replacementVisibleEnd, innerEscapeStack);
            if (innerRight >= 0) {
                resultBuilder.append(leadingEscapeStack);
                resultBuilder.append(innerEscapeStack);
                resultBuilder.append(existingLine.substring(innerRight));
            }
        }
        return resultBuilder.toString();
    }
    
    private int locate(String line, int startLocation, int startVisibleLocation, int targetVisibleLocation, StringBuilder escapeStack) {
        int length = line.length();
        int location = startLocation;
        int visibleLocation = startVisibleLocation;
        while (location < length && visibleLocation < targetVisibleLocation) {
            char c = line.charAt(location);
            if (c == ESCAPE_CHAR) {
                String ansiEscape = getAnsiEscape(line, location);
                if (ansiEscape.equals(TextUtil.ansiReset())) {
                    escapeStack.setLength(0);
                } else {
                    escapeStack.append(ansiEscape);
                }
                location += ansiEscape.length();
            } else {
                location++;
                visibleLocation++;
            }
        }
        
        if (location < length && line.substring(location).equals(TextUtil.ansiReset())) {
            escapeStack.setLength(0);
            return length;
        } else if (visibleLocation < targetVisibleLocation) {
            return visibleLocation - targetVisibleLocation;
        } else {
            return location;
        }
    }
    
    private String getAnsiEscape(String line, int position) {
        Matcher matcher = ANSI_ESCAPE_START_PATTERN.matcher(line.substring(position));
        if (!matcher.find()) {
            return ESCAPE_CHAR + "";
        }
        return matcher.group();
    }
    
}
