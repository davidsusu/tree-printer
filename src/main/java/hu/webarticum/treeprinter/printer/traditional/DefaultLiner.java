package hu.webarticum.treeprinter.printer.traditional;

import java.util.List;

import hu.webarticum.treeprinter.printer.UnicodeMode;
import hu.webarticum.treeprinter.util.LineBuffer;
import hu.webarticum.treeprinter.util.Util;

public class DefaultLiner implements Liner {

    private static final char[] LINE_CHARS_ASCII = new char[] {
        '|', ' ', '_', '|', '|', '|', '_', '|', '|', '|', ' ',  '|', '|'
    };
    
    private static final char[] LINE_CHARS_UNICODE = new char[] {
        '│', '┌', '─', '┴',  '└', '┘', '┬', '┼', '├', '┤', '┐', '│', '│'
    };
    
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
        this(UnicodeMode.isUnicodeDefault());
    }

    public DefaultLiner(boolean useUnicode) {
        this(
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[0],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[1],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[2],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[3],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[4],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[5],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[6],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[7],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[8],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[9],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[10],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[11],
            (useUnicode ? LINE_CHARS_UNICODE : LINE_CHARS_ASCII)[12],
            0, 1, true
        );
    }
    
    public DefaultLiner(
        char topConnectionChar, char bracketLeftChar, char bracketChar,
        char bracketTopChar, char bracketTopLeftChar, char bracketTopRightChar, char bracketBottomChar,
        char bracketTopAndBottomChar, char bracketTopAndBottomLeftChar, char bracketTopAndBottomRightChar,
        char bracketRightChar, char bracketOnlyChar, char bottomConnectionChar,
        int topHeight, int bottomHeight, boolean displayBracket
    ) {
        this.topConnectionChar = topConnectionChar;
        this.bracketLeftChar = bracketLeftChar;
        this.bracketChar = bracketChar;
        this.bracketTopChar = bracketTopChar;
        this.bracketTopLeftChar = bracketTopLeftChar;
        this.bracketTopRightChar = bracketTopRightChar;
        this.bracketBottomChar = bracketBottomChar;
        this.bracketTopAndBottomChar = bracketTopAndBottomChar;
        this.bracketTopAndBottomLeftChar = bracketTopAndBottomLeftChar;
        this.bracketTopAndBottomRightChar = bracketTopAndBottomRightChar;
        this.bracketRightChar = bracketRightChar;
        this.bracketOnlyChar = bracketOnlyChar;
        this.bottomConnectionChar = bottomConnectionChar;
        this.topHeight = topHeight;
        this.bottomHeight = bottomHeight;
        this.displayBracket = displayBracket;
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
        } else if (i == start) {
            return bracketLeftChar;
        } else if (i == end) {
            return bracketRightChar;
        } else {
            if (bottomConnections.contains(i)) {
                return bracketBottomChar;
            } else {
                return bracketChar;
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
    
    public static DefaultLiner.Builder createBuilder() {
        return new Builder();
    }
    
    public static class Builder {

        private int topHeight = 0;
        private int bottomHeight = 1;
        private boolean displayBracket = true;
        
        private char[] characters = (
            UnicodeMode.isUnicodeDefault() ?
            LINE_CHARS_UNICODE :
            LINE_CHARS_ASCII
        ).clone();

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
            this.characters = LINE_CHARS_ASCII.clone();
            return this;
        }

        public DefaultLiner.Builder unicode() {
            this.characters = LINE_CHARS_UNICODE.clone();
            return this;
        }

        public DefaultLiner.Builder characters(
            char topConnectionChar, char bracketLeftChar, char bracketChar,
            char bracketTopChar, char bracketTopLeftChar, char bracketTopRightChar, char bracketBottomChar,
            char bracketTopAndBottomChar, char bracketTopAndBottomLeftChar, char bracketTopAndBottomRightChar,
            char bracketRightChar, char bracketOnlyChar, char bottomConnectionChar
        ) {
            this.characters = new char[] {
                topConnectionChar, bracketLeftChar, bracketChar,
                bracketTopChar, bracketTopLeftChar, bracketTopRightChar, bracketBottomChar,
                bracketTopAndBottomChar, bracketTopAndBottomLeftChar, bracketTopAndBottomRightChar,
                bracketRightChar, bracketOnlyChar, bottomConnectionChar
            };
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
            return new DefaultLiner(
                characters[0], characters[1], characters[2], characters[3], characters[4],
                characters[5], characters[6], characters[7], characters[8], characters[9],
                characters[10], characters[11], characters[12],
                topHeight, bottomHeight, displayBracket
            );
        }
        
    }
    
}