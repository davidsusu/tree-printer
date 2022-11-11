package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.AnsiFormat;
import hu.webarticum.treeprinter.text.ConsoleText;

/**
 * {@link TreeNode} decorator implementation that applies an {@link AnsiFormat} to the node.
 */
public class AnsiFormatTreeNodeDecorator extends AbstractTreeNodeDecorator {

    private final AnsiFormat format;
    
    
    public AnsiFormatTreeNodeDecorator(TreeNode baseNode, AnsiFormat format) {
        this(baseNode, builder().format(format));
    }

    private AnsiFormatTreeNodeDecorator(TreeNode baseNode, Builder builder) {
        super(baseNode, builder.inherit, builder.decorable);
        this.format = builder.format;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    
    @Override
    public ConsoleText decoratedContent() {
        return baseNode.content().format(format);
    }

    @Override
    protected TreeNode wrapChild(TreeNode childNode, int index) {
        return builder()
                .decorable(decorable)
                .inherit(inherit)
                .format(format)
                .buildFor(childNode);
    }

    
    public static class Builder {
        
        private boolean inherit = true;

        private boolean decorable = true;

        private AnsiFormat format = AnsiFormat.NONE;
        

        public Builder inherit(boolean inherit) {
            this.inherit = inherit;
            return this;
        }

        public Builder decorable(boolean decorable) {
            this.decorable = decorable;
            return this;
        }

        public Builder format(AnsiFormat format) {
            this.format = format;
            return this;
        }
        
        public AnsiFormatTreeNodeDecorator buildFor(TreeNode node) {
            return new AnsiFormatTreeNodeDecorator(node, this);
        }
        
    }
}
