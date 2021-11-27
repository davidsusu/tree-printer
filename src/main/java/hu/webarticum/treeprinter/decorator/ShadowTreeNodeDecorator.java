package hu.webarticum.treeprinter.decorator;

import java.util.List;
import java.util.stream.Collectors;

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
    

    public ShadowTreeNodeDecorator(TreeNode decoratedNode) {
        this(decoratedNode, new Builder());
    }

    private ShadowTreeNodeDecorator(TreeNode decoratedNode, Builder builder) {
        super(decoratedNode, builder.inherit, builder.decorable);
        this.shadowChar = builder.shadowChar;
        this.verticalOffset = builder.verticalOffset;
        this.horizontalOffset = builder.horizontalOffset;
    }

    public static Builder builder() {
        return new Builder();
    }
    

    @Override
    public String decoratedContent() {
        List<String> lines = decoratedNode.content().lines().collect(Collectors.toList());
        
        int height = lines.size();
        int width = lines.stream().mapToInt(String::length).max().orElse(0);
        
        String shadowLine = buildShadowLine(width);
        String emptyPrefix = buildEmptyPrefix();
        String shadowPrefix = buildShadowPrefix();
        String shadowSuffix = buildShadowSuffix();
        
        StringBuilder resultBuilder = new StringBuilder();

        int topStart = Math.min(0, verticalOffset);
        int topEnd = Math.min(0, height + verticalOffset);
        int middleStart = Math.max(0, Math.min(height, verticalOffset));
        int middleEnd = Math.max(0, Math.min(height, height + verticalOffset));
        int bottomStart = Math.max(height, verticalOffset);
        int bottomEnd = Math.max(height, height + verticalOffset);

        for (int i = topStart; i < topEnd; i++) {
            resultBuilder.append(shadowLine);
            resultBuilder.append('\n');
        }
        for (int i = topEnd; i < 0; i++) {
            resultBuilder.append('\n');
        }
        
        for (int i = 0; i < middleStart; i++) {
            resultBuilder.append(emptyPrefix);
            resultBuilder.append(lines.get(i));
            resultBuilder.append('\n');
        }
        for (int i = middleStart; i < middleEnd; i++) {
            resultBuilder.append(shadowPrefix);
            resultBuilder.append(lines.get(i));
            resultBuilder.append(shadowSuffix);
            resultBuilder.append('\n');
        }
        for (int i = middleEnd; i < height; i++) {
            resultBuilder.append(emptyPrefix);
            resultBuilder.append(lines.get(i));
            resultBuilder.append('\n');
        }

        for (int i = height; i < bottomStart; i++) {
            resultBuilder.append('\n');
        }
        for (int i = bottomStart; i < bottomEnd; i++) {
            resultBuilder.append(shadowLine);
            resultBuilder.append('\n');
        }
        
        return resultBuilder.toString();
    }
    
    private String buildShadowLine(int width) {
        StringBuilder shadowLineBuilder = new StringBuilder();
        if (horizontalOffset > 0) {
            shadowLineBuilder.append(Util.repeat(EMPTY_CHAR, horizontalOffset));
        }
        shadowLineBuilder.append(Util.repeat(shadowChar, width));
        return shadowLineBuilder.toString();
    }

    private String buildEmptyPrefix() {
        if (horizontalOffset >= 0) {
            return "";
        }
        
        return Util.repeat(EMPTY_CHAR, -horizontalOffset);
    }

    private String buildShadowPrefix() {
        if (horizontalOffset >= 0) {
            return "";
        }
        
        return Util.repeat(shadowChar, -horizontalOffset);
    }
    
    private String buildShadowSuffix() {
        if (horizontalOffset <= 0) {
            return "";
        }
        
        return Util.repeat(shadowChar, horizontalOffset);
    }
    
    @Override
    public Insets insets() {
        Insets shadowInsets = new Insets(
                Math.max(0, -verticalOffset),
                Math.max(0, horizontalOffset),
                Math.max(0, verticalOffset),
                Math.max(0, -horizontalOffset));
        return decoratedNode.insets().extendedWith(shadowInsets);
    }
    
    @Override
    protected TreeNode wrapChild(TreeNode childNode, int index) {
        return ShadowTreeNodeDecorator.builder()
                .decorable(decorable)
                .inherit(inherit)
                .shadowChar(shadowChar)
                .verticalOffset(verticalOffset)
                .horizontalOffset(horizontalOffset)
                .buildFor(childNode)
                ;
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

        public ShadowTreeNodeDecorator buildFor(TreeNode treeNode) {
            return new ShadowTreeNodeDecorator(treeNode, this);
        }
        
    }
    
}
