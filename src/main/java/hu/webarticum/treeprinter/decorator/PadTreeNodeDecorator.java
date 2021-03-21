package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.util.Util;

public class PadTreeNodeDecorator extends AbstractTreeNodeDecorator {

    private final int topPad;
    private final int rightPad;
    private final int bottomPad;
    private final int leftPad;
    
    public PadTreeNodeDecorator(TreeNode decoratedNode) {
        this(decoratedNode, 1);
    }

    public PadTreeNodeDecorator(TreeNode decoratedNode, int pad) {
        this(decoratedNode, createBuilder().pad(pad));
    }

    private PadTreeNodeDecorator(TreeNode decoratedNode, Builder builder) {
        super(decoratedNode, builder.decorable, builder.inherit, builder.forceInherit);
        this.topPad = builder.topPad;
        this.rightPad = builder.rightPad;
        this.bottomPad = builder.bottomPad;
        this.leftPad = builder.leftPad;
    }
    
    @Override
    public String getContent() {
        String content = decoratedNode.getContent();

        String[] contentLines = content.split("\n");
        int longestLineLength = 0;
        for (String line: contentLines) {
            int lineLength = line.length();
            if (lineLength > longestLineLength) {
                longestLineLength = lineLength;
            }
        }

        StringBuilder resultBuilder = new StringBuilder();
        
        for (int i = 0; i < topPad; i++) {
            Util.repeat(resultBuilder, ' ', leftPad + longestLineLength + rightPad);
            resultBuilder.append('\n');
        }

        for (String line: contentLines) {
            Util.repeat(resultBuilder, ' ', leftPad);
            resultBuilder.append(line);
            Util.repeat(resultBuilder, ' ', longestLineLength - line.length() + rightPad);
            resultBuilder.append('\n');
        }
        
        for (int i = 0; i < bottomPad; i++) {
            Util.repeat(resultBuilder, ' ', leftPad + longestLineLength + rightPad);
            resultBuilder.append('\n');
        }
        
        return resultBuilder.toString();
    }

    @Override
    public int[] getInsets() {
        int[] innerInsets = decoratedNode.getInsets();
        return new int[] {
            innerInsets[0] + topPad,
            innerInsets[1] + rightPad,
            innerInsets[2] + bottomPad,
            innerInsets[3] + leftPad,
        };
    }
    
    @Override
    protected TreeNode decorateChild(TreeNode childNode, int index) {
        return new PadTreeNodeDecorator(
            childNode,
            createBuilder()
                .decorable(decorable)
                .inherit(inherit)
                .forceInherit(forceInherit)
                .topPad(topPad)
                .rightPad(rightPad)
                .bottomPad(bottomPad)
                .leftPad(leftPad)
        );
    }
    
    public static Builder createBuilder() {
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
        
        public PadTreeNodeDecorator buildFor(TreeNode node) {
            return new PadTreeNodeDecorator(node, this);
        }
        
    }
    
}
