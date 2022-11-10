package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.Insets;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.UnicodeMode;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;
import hu.webarticum.treeprinter.text.TextUtil;
import hu.webarticum.treeprinter.util.Util;

/**
 * {@link TreeNode} decorator implementation that draws a border around the node.
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
    
    private final AnsiFormat format;
    
    
    public BorderTreeNodeDecorator(TreeNode baseNode) {
        this(baseNode, builder());
    }

    public BorderTreeNodeDecorator(TreeNode baseNode, AnsiFormat format) {
        this(baseNode, builder().format(format));
    }

    private BorderTreeNodeDecorator(TreeNode baseNode, Builder builder) {
        super(baseNode, builder.inherit, builder.decorable);
        this.topLeft = builder.characters[0];
        this.top = builder.characters[1];
        this.topRight = builder.characters[2];
        this.right = builder.characters[3];
        this.bottomRight = builder.characters[4];
        this.bottom = builder.characters[5];
        this.bottomLeft = builder.characters[6];
        this.left = builder.characters[7];
        this.format = builder.format;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    
    @Override
    public ConsoleText decoratedContent() {
        ConsoleText baseContent = baseNode.content();
        String contentString = Util.getStringContent(baseContent);
        String[] contentLines = TextUtil.linesOf(contentString);
        int baseWidth = baseContent.dimensions().width();
        
        StringBuilder resultBuilder = new StringBuilder();
        
        resultBuilder.append(Util.getStringContent(formatBorder(composeRoofString(baseWidth))));
        resultBuilder.append('\n');
        for (String contentLine: contentLines) {
            resultBuilder.append(Util.getStringContent(formatBorder(left)));
            resultBuilder.append(contentLine);
            TextUtil.repeat(resultBuilder, ' ', baseWidth - contentLine.length());
            resultBuilder.append(Util.getStringContent(formatBorder(right)));
            resultBuilder.append('\n');
        }
        resultBuilder.append(Util.getStringContent(formatBorder(composeBeddingString(baseWidth))));
        
        String decoratedContent = resultBuilder.toString();
        return AnsiMode.isAnsiEnabled() ? ConsoleText.ofAnsi(decoratedContent) : ConsoleText.of(decoratedContent);
    }

    private ConsoleText formatBorder(char borderChar) {
        return formatBorder("" + borderChar);
    }
    
    private ConsoleText formatBorder(String borderText) {
        return ConsoleText.of(borderText).format(format);
    }

    private String composeRoofString(int innerWidth) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(topLeft);
        TextUtil.repeat(resultBuilder, top, innerWidth);
        resultBuilder.append(topRight);
        return resultBuilder.toString();
    }

    private String composeBeddingString(int innerWidth) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(bottomLeft);
        TextUtil.repeat(resultBuilder, bottom, innerWidth);
        resultBuilder.append(bottomRight);
        return resultBuilder.toString();
    }
    
    @Override
    public Insets insets() {
        return baseNode.insets().extendedWith(1);
    }
    
    @Override
    protected TreeNode wrapChild(TreeNode childNode, int index) {
        return builder()
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
                .buildFor(childNode);
    }

    
    public static class Builder {
        
        private boolean inherit = true;

        private boolean decorable = true;

        private char[] characters =
                UnicodeMode.isUnicodeDefault() ?
                BORDER_CHARS_UNICODE.clone() :
                BORDER_CHARS_ASCII.clone();
        
        private AnsiFormat format = AnsiFormat.NONE;
        

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

        public Builder format(AnsiFormat format) {
            this.format = format;
            return this;
        }
        
        public BorderTreeNodeDecorator buildFor(TreeNode node) {
            return new BorderTreeNodeDecorator(node, this);
        }
        
    }
}
