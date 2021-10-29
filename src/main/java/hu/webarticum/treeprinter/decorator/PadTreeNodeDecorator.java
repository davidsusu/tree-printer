package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.Insets;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.util.Util;

public class PadTreeNodeDecorator extends AbstractTreeNodeDecorator {

    private final Insets insets;
    
    public PadTreeNodeDecorator(TreeNode decoratedNode) {
        this(decoratedNode, 1);
    }

    public PadTreeNodeDecorator(TreeNode decoratedNode, int pad) {
        this(decoratedNode, builder().pad(pad));
    }

    public PadTreeNodeDecorator(TreeNode decoratedNode, Insets insets) {
        this(decoratedNode, builder().insets(insets));
    }

    private PadTreeNodeDecorator(TreeNode decoratedNode, Builder builder) {
        super(decoratedNode, builder.decorable, builder.inherit, builder.forceInherit);
        this.insets = new Insets(builder.topPad, builder.rightPad, builder.bottomPad, builder.leftPad);
    }
    
    @Override
    public String content() {
        String content = decoratedNode.content();

        String[] contentLines = Util.splitToLines(content);
        int contentWidth = calculateWidth(contentLines);

        StringBuilder resultBuilder = new StringBuilder();
        appendTopPadding(resultBuilder, contentWidth);
        appendPaddedContentLines(resultBuilder, contentLines, contentWidth);
        appendBottomPadding(resultBuilder, contentWidth);
        return resultBuilder.toString();
    }
    
    private int calculateWidth(String[] lines) {
        int longestLineLength = 0;
        for (String line: lines) {
            int lineLength = line.length();
            if (lineLength > longestLineLength) {
                longestLineLength = lineLength;
            }
        }
        return longestLineLength;
    }
    
    private void appendEmptyLines(StringBuilder stringBuilder, int n, int width) {
        for (int i = 0; i < n; i++) {
            Util.repeat(stringBuilder, ' ', width);
            stringBuilder.append('\n');
        }
    }

    private void appendTopPadding(StringBuilder stringBuilder, int width) {
        appendEmptyLines(stringBuilder, insets.top(), insets.left() + width + insets.right());
    }

    private void appendBottomPadding(StringBuilder stringBuilder, int width) {
        appendEmptyLines(stringBuilder, insets.bottom(), insets.left() + width + insets.right());
    }
    
    private void appendPaddedContentLines(StringBuilder stringBuilder, String[] lines, int width) {
        for (String line: lines) {
            Util.repeat(stringBuilder, ' ', insets.left());
            stringBuilder.append(line);
            Util.repeat(stringBuilder, ' ', width - line.length() + insets.right());
            stringBuilder.append('\n');
        }
    }

    @Override
    public Insets insets() {
        return decoratedNode.insets().extendedWith(insets);
    }
    
    @Override
    protected TreeNode decorateChild(TreeNode childNode, int index) {
        return new PadTreeNodeDecorator(
            childNode,
            builder()
                .decorable(decorable)
                .inherit(inherit)
                .forceInherit(forceInherit)
                .insets(insets)
        );
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {

        private boolean decorable = true;
        private boolean inherit = true;
        private boolean forceInherit = false;

        private int topPad = 0;
        private int rightPad = 0;
        private int bottomPad = 0;
        private int leftPad = 0;

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
        
        public Builder pad(int pad) {
            return pad(pad, pad, pad, pad);
        }

        public Builder pad(int topPad, int rightPad, int bottomPad, int leftPad) {
            this.topPad = topPad;
            this.rightPad = rightPad;
            this.bottomPad = bottomPad;
            this.leftPad = leftPad;
            return this;
        }

        public Builder verticalPad(int verticalPad) {
            this.topPad = verticalPad;
            this.bottomPad = verticalPad;
            return this;
        }

        public Builder horizontalPad(int horizontalPad) {
            this.leftPad = horizontalPad;
            this.rightPad = horizontalPad;
            return this;
        }

        public Builder topPad(int topPad) {
            this.topPad = topPad;
            return this;
        }

        public Builder rightPad(int rightPad) {
            this.rightPad = rightPad;
            return this;
        }

        public Builder bottomPad(int bottomPad) {
            this.bottomPad = bottomPad;
            return this;
        }

        public Builder leftPad(int leftPad) {
            this.leftPad = leftPad;
            return this;
        }

        public Builder insets(Insets insets) {
            return pad(insets.top(), insets.right(), insets.bottom(), insets.left());
        }

        public PadTreeNodeDecorator buildFor(TreeNode node) {
            return new PadTreeNodeDecorator(node, this);
        }
        
    }
    
}
