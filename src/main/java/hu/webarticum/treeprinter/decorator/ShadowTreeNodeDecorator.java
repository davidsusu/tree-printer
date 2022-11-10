package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.Insets;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.UnicodeMode;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;
import hu.webarticum.treeprinter.text.Dimensions;
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
 * 
 * Hello, Node!
 *  ▒▒▒▒▒▒▒▒▒▒▒▒
 * </pre>
 */
public class ShadowTreeNodeDecorator extends AbstractTreeNodeDecorator {
    
    private static final char EMPTY_CHAR = ' ';

    private static final char UNICODE_SHADOW_CHAR = '\u2592';

    private static final char ASCII_SHADOW_CHAR = '#';
    
    
    private final char shadowChar;
    
    private final int verticalOffset;
    
    private final int horizontalOffset;
    
    private final AnsiFormat format;
    

    public ShadowTreeNodeDecorator(TreeNode baseNode) {
        this(baseNode, builder());
    }

    public ShadowTreeNodeDecorator(TreeNode baseNode, AnsiFormat format) {
        this(baseNode, builder().format(format));
    }

    private ShadowTreeNodeDecorator(TreeNode baseNode, Builder builder) {
        super(baseNode, builder.inherit, builder.decorable);
        this.shadowChar = builder.shadowChar;
        this.verticalOffset = builder.verticalOffset;
        this.horizontalOffset = builder.horizontalOffset;
        this.format = builder.format;
    }

    public static Builder builder() {
        return new Builder();
    }
    

    @Override
    public ConsoleText decoratedContent() {
        ConsoleText baseContent = baseNode.content();
        ConsoleText[] baseLines = TextUtil.linesOf(baseContent);
        Dimensions baseDimensions = baseContent.dimensions();
        int baseWidth = baseDimensions.width();
        int baseHeight = baseDimensions.height();
        
        String shadowLine = buildShadowLine(baseWidth);
        String shadowEmptyPrefix = buildShadowEmptyPrefix();
        String emptyPrefix = buildEmptyPrefix();
        String shadowPrefix = buildShadowPrefix();
        String shadowSuffix = buildShadowSuffix();
        
        StringBuilder resultBuilder = new StringBuilder();

        int topStart = Math.min(0, verticalOffset);
        int topEnd = Math.min(0, baseHeight + verticalOffset);
        int middleStart = Math.max(0, Math.min(baseHeight, verticalOffset));
        int middleEnd = Math.max(0, Math.min(baseHeight, baseHeight + verticalOffset));
        int bottomStart = Math.max(baseHeight, verticalOffset);
        int bottomEnd = Math.max(baseHeight, baseHeight + verticalOffset);

        for (int i = topStart; i < topEnd; i++) {
            resultBuilder.append(shadowEmptyPrefix);
            resultBuilder.append(shadowLine);
            resultBuilder.append('\n');
        }
        for (int i = topEnd; i < 0; i++) {
            resultBuilder.append('\n');
        }
        
        for (int i = 0; i < middleStart; i++) {
            resultBuilder.append(emptyPrefix);
            resultBuilder.append(Util.getStringContent(baseLines[i]));
            resultBuilder.append('\n');
        }
        for (int i = middleStart; i < middleEnd; i++) {
            resultBuilder.append(Util.getStringContent(formatShadow(shadowPrefix)));
            resultBuilder.append(Util.getStringContent(baseLines[i]));
            TextUtil.repeat(resultBuilder, ' ', baseWidth - baseLines[i].dimensions().width());
            resultBuilder.append(Util.getStringContent(formatShadow(shadowSuffix)));
            resultBuilder.append('\n');
        }
        for (int i = middleEnd; i < baseHeight; i++) {
            resultBuilder.append(emptyPrefix);
            resultBuilder.append(Util.getStringContent(baseLines[i]));
            resultBuilder.append('\n');
        }

        for (int i = baseHeight; i < bottomStart; i++) {
            resultBuilder.append('\n');
        }
        for (int i = bottomStart; i < bottomEnd; i++) {
            resultBuilder.append(shadowEmptyPrefix);
            resultBuilder.append(Util.getStringContent(formatShadow(shadowLine)));
            resultBuilder.append('\n');
        }

        String decoratedContent = resultBuilder.toString();
        return AnsiMode.isAnsiEnabled() ? ConsoleText.ofAnsi(decoratedContent) : ConsoleText.of(decoratedContent);
    }

    private ConsoleText formatShadow(String shadowText) {
        return ConsoleText.of(shadowText).format(format);
    }

    private String buildShadowLine(int width) {
        return TextUtil.repeat(shadowChar, width);
    }

    private String buildShadowEmptyPrefix() {
        if (horizontalOffset <= 0) {
            return "";
        }
        
        return TextUtil.repeat(EMPTY_CHAR, horizontalOffset);
    }

    private String buildEmptyPrefix() {
        if (horizontalOffset >= 0) {
            return "";
        }
        
        return TextUtil.repeat(EMPTY_CHAR, -horizontalOffset);
    }

    private String buildShadowPrefix() {
        if (horizontalOffset >= 0) {
            return "";
        }
        
        return TextUtil.repeat(shadowChar, -horizontalOffset);
    }
    
    private String buildShadowSuffix() {
        if (horizontalOffset <= 0) {
            return "";
        }
        
        return TextUtil.repeat(shadowChar, horizontalOffset);
    }
    
    @Override
    public Insets insets() {
        Insets shadowInsets = new Insets(
                Math.max(0, -verticalOffset),
                Math.max(0, horizontalOffset),
                Math.max(0, verticalOffset),
                Math.max(0, -horizontalOffset));
        return baseNode.insets().extendedWith(shadowInsets);
    }
    
    @Override
    protected TreeNode wrapChild(TreeNode childNode, int index) {
        return builder()
                .decorable(decorable)
                .inherit(inherit)
                .shadowChar(shadowChar)
                .verticalOffset(verticalOffset)
                .horizontalOffset(horizontalOffset)
                .buildFor(childNode);
    }
    
    public static class Builder {
        
        private boolean inherit = true;
        
        private boolean decorable = true;

        private char shadowChar =
                UnicodeMode.isUnicodeDefault() ?
                UNICODE_SHADOW_CHAR :
                ASCII_SHADOW_CHAR;
        
        private int verticalOffset = 1;
        
        private int horizontalOffset = 1;
        
        private AnsiFormat format = AnsiFormat.NONE;


        public Builder inherit(boolean inherit) {
            this.inherit = inherit;
            return this;
        }

        public Builder decorable(boolean decorable) {
            this.decorable = decorable;
            return this;
        }

        public Builder shadowChar(char shadowChar) {
            this.shadowChar = shadowChar;
            return this;
        }

        public Builder verticalOffset(int verticalOffset) {
            this.verticalOffset = verticalOffset;
            return this;
        }

        public Builder horizontalOffset(int horizontalOffset) {
            this.horizontalOffset = horizontalOffset;
            return this;
        }

        public Builder format(AnsiFormat format) {
            this.format = format;
            return this;
        }

        public ShadowTreeNodeDecorator buildFor(TreeNode node) {
            return new ShadowTreeNodeDecorator(node, this);
        }
        
    }
    
}
