package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.Insets;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.printer.UnicodeMode;
import hu.webarticum.treeprinter.util.Util;

public class BorderTreeNodeDecorator extends AbstractTreeNodeDecorator {

    private static final char[] BORDER_CHARS_ASCII = new char[] {
        '.', '-', '.', '|', '\'', '-', '`', '|'
    };
    
    private static final char[] BORDER_CHARS_UNICODE = new char[] {
        '┌', '─', '┐', '│', '┘', '─', '└', '│',
    };

    private static final char[] BORDER_CHARS_WIDE_UNICODE = new char[] {
        '\u259B', '\u2594', '\u259C', '\u2595', '\u259F', '\u2581', '\u2599', '\u258F'
    };
    
    private final char topLeft;
    private final char top;
    private final char topRight;
    private final char right;
    private final char bottomRight;
    private final char bottom;
    private final char bottomLeft;
    private final char left;
    
    public BorderTreeNodeDecorator(TreeNode decoratedNode) {
        this(decoratedNode, createBuilder());
    }

    private BorderTreeNodeDecorator(TreeNode decoratedNode, Builder builder) {
        super(decoratedNode, builder.decorable, builder.inherit, builder.forceInherit);
        this.topLeft = builder.characters[0];
        this.top = builder.characters[1];
        this.topRight = builder.characters[2];
        this.right = builder.characters[3];
        this.bottomRight = builder.characters[4];
        this.bottom = builder.characters[5];
        this.bottomLeft = builder.characters[6];
        this.left = builder.characters[7];
    }

    
    @Override
    public String getContent() {
        String content = decoratedNode.getContent();
        
        String[] contentLines = Util.splitToLines(content);
        int longestLineLength = 0;
        for (String line: contentLines) {
            int lineLength = line.length();
            if (lineLength > longestLineLength) {
                longestLineLength = lineLength;
            }
        }
        
        StringBuilder resultBuilder = new StringBuilder();
        
        resultBuilder.append(topLeft);
        Util.repeat(resultBuilder, top, longestLineLength);
        resultBuilder.append(topRight);
        resultBuilder.append("\n");
        for (String contentLine: contentLines) {
            resultBuilder.append(left);
            resultBuilder.append(contentLine);
            Util.repeat(resultBuilder, ' ', longestLineLength - contentLine.length());
            resultBuilder.append(right);
            resultBuilder.append("\n");
        }
        resultBuilder.append(bottomLeft);
        Util.repeat(resultBuilder, bottom, longestLineLength);
        resultBuilder.append(bottomRight);
        
        return resultBuilder.toString();
    }

    @Override
    public Insets getInsets() {
        return decoratedNode.getInsets().extendedWith(1);
    }
    
    @Override
    protected TreeNode decorateChild(TreeNode childNode, int index) {
        return new BorderTreeNodeDecorator(
            childNode,
            createBuilder()
                .decorable(decorable)
                .inherit(inherit)
                .forceInherit(forceInherit)
                .topLeft(topLeft)
                .top(top)
                .topRight(topRight)
                .right(right)
                .bottomRight(bottomRight)
                .bottom(bottom)
                .bottomLeft(bottomLeft)
                .left(left)
        );
    }

    public static Builder createBuilder() {
        return new Builder();
    }
    
    public static class Builder {

        private boolean decorable = true;
        private boolean inherit = true;
        private boolean forceInherit = false;

        private char[] characters = (
            UnicodeMode.isUnicodeDefault() ?
            BORDER_CHARS_UNICODE :
            BORDER_CHARS_ASCII
        ).clone();
        
        public Builder decorable(boolean decorable) {
            this.decorable = decorable;
            return this;
        }

        public Builder inherit(boolean inherit) {
            this.inherit = inherit;
            return this;
        }

        public Builder inherit(boolean inherit, boolean forceInherit) {
            this.inherit = inherit;
            this.forceInherit = forceInherit;
            return this;
        }

        public Builder forceInherit(boolean forceInherit) {
            this.forceInherit = forceInherit;
            return this;
        }
        
        public Builder ascii() {
            this.characters = BORDER_CHARS_ASCII.clone();
            return this;
        }
        
        public Builder unicode() {
            this.characters = BORDER_CHARS_UNICODE.clone();
            return this;
        }

        public Builder wideUnicode() {
            this.characters = BORDER_CHARS_WIDE_UNICODE.clone();
            return this;
        }
        
        public Builder topLeft(char topLeft) {
            this.characters[0] = topLeft;
            return this;
        }

        public Builder top(char top) {
            this.characters[1] = top;
            return this;
        }

        public Builder topRight(char topRight) {
            this.characters[2] = topRight;
            return this;
        }

        public Builder right(char right) {
            this.characters[3] = right;
            return this;
        }

        public Builder bottomRight(char bottomRight) {
            this.characters[4] = bottomRight;
            return this;
        }

        public Builder bottom(char bottom) {
            this.characters[5] = bottom;
            return this;
        }

        public Builder bottomLeft(char bottomLeft) {
            this.characters[6] = bottomLeft;
            return this;
        }

        public Builder left(char left) {
            this.characters[7] = left;
            return this;
        }
        
        public BorderTreeNodeDecorator buildFor(TreeNode node) {
            return new BorderTreeNodeDecorator(node, this);
        }
        
    }
}
