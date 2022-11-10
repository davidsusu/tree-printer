package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.HorizontalAlign;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.VerticalAlign;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;
import hu.webarticum.treeprinter.text.Dimensions;
import hu.webarticum.treeprinter.text.TextUtil;
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
    
    private final AnsiFormat backgroundFormat;
    

    public JustifyTreeNodeDecorator(TreeNode decoratedNode) {
        this(decoratedNode, builder());
    }

    public JustifyTreeNodeDecorator(TreeNode decoratedNode, AnsiFormat backgroundFormat) {
        this(decoratedNode, builder().backgroundFormat(backgroundFormat));
    }

    public JustifyTreeNodeDecorator(TreeNode decoratedNode, HorizontalAlign textAlign, AnsiFormat backgroundFormat) {
        this(decoratedNode, builder().horizontalAlign(textAlign).backgroundFormat(backgroundFormat));
    }

    private JustifyTreeNodeDecorator(TreeNode decoratedNode, Builder builder) {
        super(decoratedNode, builder.inherit, builder.decorable);
        this.minimumWidth = builder.minimumWidth;
        this.minimumHeight = builder.minimumHeight;
        this.horizontalAlign = builder.horizontalAlign;
        this.verticalAlign = builder.verticalAlign;
        this.background = builder.background;
        this.backgroundFormat = builder.backgroundFormat;
    }

    public static Builder builder() {
        return new Builder();
    }
    

    @Override
    protected ConsoleText decoratedContent() {
        ConsoleText baseContent = baseNode.content();
        ConsoleText[] baseLines = TextUtil.linesOf(baseContent);
        Dimensions baseDimensions = baseContent.dimensions();
        int fullWidth = Math.max(minimumWidth, baseDimensions.width());
        int fullHeight = Math.max(minimumHeight, baseDimensions.height());
        int topPad = getStartPad(fullHeight, baseDimensions.height(), verticalAlign);
        int bottomPad = fullHeight - baseDimensions.height() - topPad;
        
        StringBuilder resultBuilder = new StringBuilder();
        appendTopLines(resultBuilder, fullWidth, topPad);
        appendMiddleLines(resultBuilder, baseLines, fullWidth);
        appendBottomLines(resultBuilder, fullWidth, bottomPad);

        String decoratedContent = resultBuilder.toString();
        return Util.toConsoleText(decoratedContent);
        
    }
    
    private void appendTopLines(StringBuilder contentBuilder, int width, int height) {
        for (int i = 0; i < height; i++) {
            contentBuilder.append(Util.getStringContent(composeBackground(width)));
            contentBuilder.append('\n');
        }
    }

    private void appendMiddleLines(StringBuilder contentBuilder, ConsoleText[] baseLines, int fullWidth) {
        boolean first = true;
        for (ConsoleText baseLine : baseLines) {
            if (first) {
                first = false;
            } else {
                contentBuilder.append('\n');
            }
            appendMiddleLine(contentBuilder, baseLine, fullWidth);
        }
    }

    private void appendMiddleLine(StringBuilder contentBuilder, ConsoleText baseLine, int fullWidth) {
        int baseLineWidth = baseLine.dimensions().width();
        int leftPad = getStartPad(fullWidth, baseLineWidth, horizontalAlign);
        int rightPad = fullWidth - baseLineWidth - leftPad;
        contentBuilder.append(Util.getStringContent(composeBackground(leftPad)));
        contentBuilder.append(Util.getStringContent(baseLine));
        contentBuilder.append(Util.getStringContent(composeBackground(rightPad)));
    }
    
    private void appendBottomLines(StringBuilder contentBuilder, int width, int height) {
        for (int i = 0; i < height; i++) {
            contentBuilder.append('\n');
            contentBuilder.append(Util.getStringContent(composeBackground(width)));
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
    
    private ConsoleText composeBackground(int width) {
        return ConsoleText.of(TextUtil.repeat(background, width)).format(backgroundFormat);
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
        
        private AnsiFormat backgroundFormat = AnsiFormat.NONE;

        
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

        public Builder backgroundFormat(AnsiFormat backgroundFormat) {
            this.backgroundFormat = backgroundFormat;
            return this;
        }
        
        public JustifyTreeNodeDecorator buildFor(TreeNode node) {
            return new JustifyTreeNodeDecorator(node, this);
        }
        
    }
    
}
