package hu.webarticum.treeprinter.printer.traditional;

import java.util.List;

import hu.webarticum.treeprinter.UnicodeMode;
import hu.webarticum.treeprinter.util.LineBuffer;
import hu.webarticum.treeprinter.util.Util;

public class DefaultLiner implements Liner {

    private static final char[] LINE_CHARS_ASCII = new char[] {
            '|', ' ', '_', '|', '|', '|', '_', '|', '|', '|', ' ',  '|', '|' };
    
    private static final char[] LINE_CHARS_UNICODE = new char[] {
            '│', '┌', '─', '┴', '└', '┘', '┬', '┼', '├', '┤', '┐', '│', '│' };
    
    
    private final char topConnectionChar;
    
    private final char bracketLeftChar;
    
    private final char bracketChar;
    
    private final char bracketTopChar;
    
    private final char bracketTopLeftChar;
    
    private final char bracketTopRightChar;
    
    private final char bracketBottomChar;
    
    private final char bracketTopAndBottomChar;
    
    private final char bracketTopAndBottomLeftChar;
    
    private final char bracketTopAndBottomRightChar;
    
    private final char bracketRightChar;
    
    private final char bracketOnlyChar;
    
    private final char bottomConnectionChar;

    private final int topHeight;
    
    private final int bottomHeight;

    private final boolean displayBracket;

    
    public DefaultLiner() {
        this(builder());
    }

    public DefaultLiner(boolean useUnicode) {
        this(builder().unicode(useUnicode));
    }
    
    private DefaultLiner(Builder builder) {
        this.topConnectionChar = builder.characters[0];
        this.bracketLeftChar = builder.characters[1];
        this.bracketChar = builder.characters[2];
        this.bracketTopChar = builder.characters[3];
        this.bracketTopLeftChar = builder.characters[4];
        this.bracketTopRightChar = builder.characters[5];
        this.bracketBottomChar =builder.characters[6];
        this.bracketTopAndBottomChar = builder.characters[7];
        this.bracketTopAndBottomLeftChar = builder.characters[8];
        this.bracketTopAndBottomRightChar = builder.characters[9];
        this.bracketRightChar = builder.characters[10];
        this.bracketOnlyChar = builder.characters[11];
        this.bottomConnectionChar = builder.characters[12];
        this.topHeight = builder.topHeight;
        this.bottomHeight = builder.bottomHeight;
        this.displayBracket = builder.displayBracket;
    }

    public static DefaultLiner.Builder builder() {
        return new Builder();
    }
    
    
    @Override
    public int printConnections(LineBuffer buffer, int row, int topConnection, List<Integer> bottomConnections) {
        int start = Math.min(topConnection, bottomConnections.get(0));
        int end = Math.max(topConnection, bottomConnections.get(bottomConnections.size() - 1));
        int topHeightWithBracket = topHeight + (displayBracket ? 1 : 0);
        int fullHeight = topHeightWithBracket + bottomHeight;
        
        printTopConnection(buffer, row, start, topConnection);
        printConnectionBracketLine(buffer, row, start, end, topConnection, bottomConnections);
        printBottomConnections(buffer, row, start, topHeightWithBracket, fullHeight, bottomConnections);
        
        return fullHeight;
    }
    
    private void printTopConnection(LineBuffer buffer, int row, int start, int topConnection) {
        StringBuilder topConnectionLineBuilder = new StringBuilder();
        Util.repeat(topConnectionLineBuilder, ' ', topConnection - start);
        topConnectionLineBuilder.append(topConnectionChar);
        String topConnectionLine = topConnectionLineBuilder.toString();
        for (int i = 0; i < topHeight; i++) {
            buffer.write(row + i, start, topConnectionLine);
        }
    }
    
    private void printConnectionBracketLine(LineBuffer buffer, int row, int start, int end, int topConnection, List<Integer> bottomConnections) {
        StringBuilder bracketLineBuilder = new StringBuilder();
        for (int i = start; i <= end; i++) {
            char lineCharacter = getNthBracketLineChar(i, start, end, topConnection, bottomConnections);
            bracketLineBuilder.append(lineCharacter);
        }
        buffer.write(row + topHeight, start, bracketLineBuilder.toString());
    }
    
    private char getNthBracketLineChar(int i, int start, int end, int topConnection, List<Integer> bottomConnections) {
        if (start == end) {
            return bracketOnlyChar;
        } else if (i == topConnection) {
            return getBracketLineCharAtTopConnection(topConnection, start, end, bottomConnections);
        } else if (i == start) {
            return bracketLeftChar;
        } else if (i == end) {
            return bracketRightChar;
        } else if (bottomConnections.contains(i)) {
            return bracketBottomChar;
        } else {
            return bracketChar;
        }
    }
    
    private char getBracketLineCharAtTopConnection(int i, int start, int end, List<Integer> bottomConnections) {
        if (bottomConnections.contains(i)) {
            if (i == start) {
                return bracketTopAndBottomLeftChar;
            } else if (i == end) {
                return bracketTopAndBottomRightChar;
            } else {
                return bracketTopAndBottomChar;
            }
        } else {
            if (i == start) {
                return bracketTopLeftChar;
            } else if (i == end) {
                return bracketTopRightChar;
            } else {
                return bracketTopChar;
            }
        }
    }

    private void printBottomConnections(LineBuffer buffer, int row, int start, int topHeightWithBracket, int fullHeight, List<Integer> bottomConnections) {
        StringBuilder bottomConnectionLineBuilder = new StringBuilder();
        int position = start;
        for (int bottomConnection: bottomConnections) {
            for (int i = position; i < bottomConnection; i++) {
                bottomConnectionLineBuilder.append(' ');
            }
            bottomConnectionLineBuilder.append(bottomConnectionChar);
            position = bottomConnection + 1;
        }
        String bottomConnectionLine = bottomConnectionLineBuilder.toString();
        for (int i = topHeightWithBracket; i < fullHeight; i++) {
            buffer.write(row + i, start, bottomConnectionLine);
        }
    }
    
    
    public static class Builder {

        private int topHeight = 0;
        
        private int bottomHeight = 1;
        
        private boolean displayBracket = true;
        
        private char[] characters =
                UnicodeMode.isUnicodeDefault() ?
                LINE_CHARS_UNICODE.clone() :
                LINE_CHARS_ASCII.clone();

                
        public DefaultLiner.Builder topHeight(int topHeight) {
            this.topHeight = topHeight;
            return this;
        }

        public DefaultLiner.Builder bottomHeight(int bottomHeight) {
            this.bottomHeight = bottomHeight;
            return this;
        }

        public DefaultLiner.Builder displayBracket(boolean displayBracket) {
            this.displayBracket = displayBracket;
            return this;
        }

        public DefaultLiner.Builder ascii() {
            return unicode(false);
        }

        public DefaultLiner.Builder unicode() {
            return unicode(true);
        }

        public DefaultLiner.Builder unicode(boolean useUnicode) {
            this.characters = useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII;
            return this;
        }

        public DefaultLiner.Builder topConnectionChar(char topConnectionChar) {
            this.characters[0] = topConnectionChar;
            return this;
        }

        public DefaultLiner.Builder bracketLeftChar(char bracketLeftChar) {
            this.characters[1] = bracketLeftChar;
            return this;
        }

        public DefaultLiner.Builder bracketChar(char bracketChar) {
            this.characters[2] = bracketChar;
            return this;
        }

        public DefaultLiner.Builder bracketTopChar(char bracketTopChar) {
            this.characters[3] = bracketTopChar;
            return this;
        }

        public DefaultLiner.Builder bracketTopLeftChar(char bracketTopLeftChar) {
            this.characters[4] = bracketTopLeftChar;
            return this;
        }

        public DefaultLiner.Builder bracketTopRightChar(char bracketTopRightChar) {
            this.characters[5] = bracketTopRightChar;
            return this;
        }

        public DefaultLiner.Builder bracketBottomChar(char bracketBottomChar) {
            this.characters[6] = bracketBottomChar;
            return this;
        }

        public DefaultLiner.Builder bracketTopAndBottomChar(char bracketTopAndBottomChar) {
            this.characters[7] = bracketTopAndBottomChar;
            return this;
        }

        public DefaultLiner.Builder bracketTopAndBottomLeftChar(char bracketTopAndBottomLeftChar) {
            this.characters[8] = bracketTopAndBottomLeftChar;
            return this;
        }

        public DefaultLiner.Builder bracketTopAndBottomRightChar(char bracketTopAndBottomRightChar) {
            this.characters[9] = bracketTopAndBottomRightChar;
            return this;
        }

        public DefaultLiner.Builder bracketRightChar(char bracketRightChar) {
            this.characters[10] = bracketRightChar;
            return this;
        }

        public DefaultLiner.Builder bracketOnlyChar(char bracketOnlyChar) {
            this.characters[11] = bracketOnlyChar;
            return this;
        }

        public DefaultLiner.Builder bottomConnectionChar(char bottomConnectionChar) {
            this.characters[12] = bottomConnectionChar;
            return this;
        }

        public DefaultLiner build() {
            return new DefaultLiner(this);
        }
        
    }
    
}