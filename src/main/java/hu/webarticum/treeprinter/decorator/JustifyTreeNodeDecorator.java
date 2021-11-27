package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.HorizontalAlign;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.VerticalAlign;
import hu.webarticum.treeprinter.util.Util;

/**
 * {@link TreeNode} decorator implementation with justifying functionality.
 * 
 * <p>You can set a minimum width and height,
 * vertical and horizontal align,
 * and the character that fills background area.</p>
 *
 * This decorator ensures that the generated content will have a rectangle shape.
 *
 * <p>Given the following content:</p>
 * 
 * <pre>
 * Hello, Node!
 * Line 2
 * </pre>
 * 
 * <p>By setting <code>minimumHeight</code> to <code>6</code>,
 * <code>minimumWidth</code> to <code>20</code>,
 * <code>verticalAlign</code> to <code>MIDDLE</code>,
 * <code>horizontalAlign</code> to <code>RIGHT</code>
 * and <code>background</code> to <code>~</code>,
 * the following content will be produced:
 * </p>
 * 
 * <pre>
 * ~~~~~~~~~~~~~~~~~~~~
 * ~~~~~~~~~~~~~~~~~~~~
 * ~~~~~~~~Hello, Node!
 * ~~~~~~~~~~~~~~Line 2
 * ~~~~~~~~~~~~~~~~~~~~
 * ~~~~~~~~~~~~~~~~~~~~
 * </pre>
 */
public class JustifyTreeNodeDecorator extends AbstractTreeNodeDecorator {
    
    private final int minimumWidth;
    
    private final int minimumHeight;
    
    private final HorizontalAlign horizontalAlign;
    
    private final VerticalAlign verticalAlign;
    
    private final char background;
    

    public JustifyTreeNodeDecorator(TreeNode decoratedNode) {
        this(decoratedNode, builder());
    }

    public JustifyTreeNodeDecorator(TreeNode decoratedNode, HorizontalAlign textAlign) {
        this(decoratedNode, builder().horizontalAlign(textAlign));
    }

    private JustifyTreeNodeDecorator(TreeNode decoratedNode, Builder builder) {
        super(decoratedNode, builder.inherit, builder.decorable);
        this.minimumWidth = builder.minimumWidth;
        this.minimumHeight = builder.minimumHeight;
        this.horizontalAlign = builder.horizontalAlign;
        this.verticalAlign = builder.verticalAlign;
        this.background = builder.background;
    }

    public static Builder builder() {
        return new Builder();
    }
    

    @Override
    protected String decoratedContent() {
        String baseContent = baseNode.content();
        String[] baseLines = Util.splitToLines(baseContent);
        int contentHeight = baseLines.length;
        int contentWidth = Util.getMaxLength(baseLines);
        int fullWidth = Math.max(minimumWidth, contentWidth);
        int fullHeight = Math.max(minimumHeight, contentHeight);
        int topPad = getStartPad(fullHeight, contentHeight, verticalAlign);
        int bottomPad = fullHeight - contentHeight - topPad;
        
        StringBuilder resultBuilder = new StringBuilder();
        appendTopLines(resultBuilder, fullWidth, topPad);
        appendMiddleLines(resultBuilder, baseLines, fullWidth);
        appendBottomLines(resultBuilder, fullWidth, bottomPad);
        return resultBuilder.toString();
        
    }
    
    private void appendTopLines(StringBuilder contentBuilder, int width, int height) {
        for (int i = 0; i < height; i++) {
            contentBuilder.append(Util.repeat(background, width));
            contentBuilder.append('\n');
        }
    }

    private void appendMiddleLines(StringBuilder contentBuilder, String[] baseLines, int fullWidth) {
        boolean first = true;
        for (String baseLine : baseLines) {
            if (first) {
                first = false;
            } else {
                contentBuilder.append('\n');
            }
            appendMiddleLine(contentBuilder, baseLine, fullWidth);
        }
    }

    private void appendMiddleLine(StringBuilder contentBuilder, String baseLine, int fullWidth) {
        int baseLineLength = baseLine.length();
        int leftPad = getStartPad(fullWidth, baseLineLength, horizontalAlign);
        int rightPad = fullWidth - baseLineLength - leftPad;
        contentBuilder.append(Util.repeat(background, leftPad));
        contentBuilder.append(baseLine);
        contentBuilder.append(Util.repeat(background, rightPad));
    }
    
    private void appendBottomLines(StringBuilder contentBuilder, int width, int height) {
        for (int i = 0; i < height; i++) {
            contentBuilder.append('\n');
            contentBuilder.append(Util.repeat(background, width));
        }
    }

    private int getStartPad(int fullSize, int contentSize, Object alignType) {
        if (alignType == HorizontalAlign.LEFT || alignType == VerticalAlign.TOP) {
            return 0;
        }
        int remainingSize = fullSize - contentSize;
        if (alignType == HorizontalAlign.RIGHT || alignType == VerticalAlign.BOTTOM) {
            return remainingSize;
        } else {
            return remainingSize / 2;
        }
    }
    
    @Override
    protected TreeNode wrapChild(TreeNode childNode, int index) {
        return new JustifyTreeNodeDecorator(
                childNode,
                builder()
                        .decorable(decorable)
                        .inherit(inherit)
                        .minimumWidth(minimumWidth)
                        .minimumHeight(minimumHeight)
                        .horizontalAlign(horizontalAlign)
                        .verticalAlign(verticalAlign)
                        .background(background)
                );
    }


    public static class Builder {
        
        private boolean inherit = true;

        private boolean decorable = true;
        
        private int minimumWidth = 0;
        
        private int minimumHeight = 0;
        
        private HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;
        
        private VerticalAlign verticalAlign = VerticalAlign.TOP;
        
        private char background = ' ';

        
        public Builder inherit(boolean inherit) {
            this.inherit = inherit;
            return this;
        }

        public Builder decorable(boolean decorable) {
            this.decorable = decorable;
            return this;
        }

        public Builder minimumWidth(int minimumWidth) {
            this.minimumWidth = minimumWidth;
            return this;
        }

        public Builder minimumHeight(int minimumHeight) {
            this.minimumHeight = minimumHeight;
            return this;
        }

        public Builder horizontalAlign(HorizontalAlign horizontalAlign) {
            this.horizontalAlign = horizontalAlign;
            return this;
        }

        public Builder verticalAlign(VerticalAlign verticalAlign) {
            this.verticalAlign = verticalAlign;
            return this;
        }

        public Builder background(char background) {
            this.background = background;
            return this;
        }
        
        public JustifyTreeNodeDecorator buildFor(TreeNode node) {
            return new JustifyTreeNodeDecorator(node, this);
        }
        
    }
    
}
