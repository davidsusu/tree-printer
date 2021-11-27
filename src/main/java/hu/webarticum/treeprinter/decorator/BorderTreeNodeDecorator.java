package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.Insets;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.UnicodeMode;
import hu.webarticum.treeprinter.util.Util;

/**
 * {@link TreeNode} implementation that draws a border around the node.
 * 
 * <p>For example, with the default options this content:</p>
 * 
 * <pre>
 * Hello, Node!
 * </pre>
 * 
 * <p>will be transformed to this:</p>
 * 
 * <pre>
 * ┌────────────┐
 * │Hello, Node!│
 * └────────────┘
 * </pre>
 */
public class BorderTreeNodeDecorator extends AbstractTreeNodeDecorator {

    private static final char[] BORDER_CHARS_ASCII = new char[] {
            '.', '-', '.', '|', '\'', '-', '`', '|' };
    
    private static final char[] BORDER_CHARS_UNICODE = new char[] {
            '┌', '─', '┐', '│', '┘', '─', '└', '│' };

    private static final char[] BORDER_CHARS_WIDE_UNICODE = new char[] {
            '\u259B', '\u2594', '\u259C', '\u2595', '\u259F', '\u2581', '\u2599', '\u258F' };
    
    
    private final char topLeft;
    
    private final char top;
    
    private final char topRight;
    
    private final char right;
    
    private final char bottomRight;
    
    private final char bottom;
    
    private final char bottomLeft;
    
    private final char left;
    
    
    public BorderTreeNodeDecorator(TreeNode decoratedNode) {
        this(decoratedNode, builder());
    }

    private BorderTreeNodeDecorator(TreeNode decoratedNode, Builder builder) {
        super(decoratedNode, builder.inherit, builder.decorable);
        this.topLeft = builder.characters[0];
        this.top = builder.characters[1];
        this.topRight = builder.characters[2];
        this.right = builder.characters[3];
        this.bottomRight = builder.characters[4];
        this.bottom = builder.characters[5];
        this.bottomLeft = builder.characters[6];
        this.left = builder.characters[7];
    }

    public static Builder builder() {
        return new Builder();
    }
    
    
    @Override
    public String decoratedContent() {
        String content = decoratedNode.content();
        
        String[] contentLines = Util.splitToLines(content);
        int longestLineLength = Util.getMaxLength(contentLines);
        
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
    public Insets insets() {
        return decoratedNode.insets().extendedWith(1);
    }
    
    @Override
    protected TreeNode wrapChild(TreeNode childNode, int index) {
        return new BorderTreeNodeDecorator(
                childNode,
                builder()
                        .decorable(decorable)
                        .inherit(inherit)
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

    
    public static class Builder {
        
        private boolean inherit = true;

        private boolean decorable = true;

        private char[] characters =
                UnicodeMode.isUnicodeDefault() ?
                BORDER_CHARS_UNICODE.clone() :
                BORDER_CHARS_ASCII.clone();
        

        public Builder inherit(boolean inherit) {
            this.inherit = inherit;
            return this;
        }

        public Builder decorable(boolean decorable) {
            this.decorable = decorable;
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
