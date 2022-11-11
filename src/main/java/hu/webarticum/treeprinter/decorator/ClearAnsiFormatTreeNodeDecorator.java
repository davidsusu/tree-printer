package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.ConsoleText;

/**
 * {@link TreeNode} decorator implementation that clears all ANSI formatters from the node.
 */
public class ClearAnsiFormatTreeNodeDecorator extends AbstractTreeNodeDecorator {

    public ClearAnsiFormatTreeNodeDecorator(TreeNode baseNode) {
        this(baseNode, builder());
    }

    private ClearAnsiFormatTreeNodeDecorator(TreeNode baseNode, Builder builder) {
        super(baseNode, builder.inherit, builder.decorable);
    }

    public static Builder builder() {
        return new Builder();
    }
    
    
    @Override
    public ConsoleText decoratedContent() {
        return ConsoleText.of(baseNode.content().plain());
    }

    @Override
    protected TreeNode wrapChild(TreeNode childNode, int index) {
        return builder()
                .decorable(decorable)
                .inherit(inherit)
                .buildFor(childNode);
    }

    
    public static class Builder {
        
        private boolean inherit = true;

        private boolean decorable = true;
        

        public Builder inherit(boolean inherit) {
            this.inherit = inherit;
            return this;
        }

        public Builder decorable(boolean decorable) {
            this.decorable = decorable;
            return this;
        }

        public ClearAnsiFormatTreeNodeDecorator buildFor(TreeNode node) {
            return new ClearAnsiFormatTreeNodeDecorator(node, this);
        }
        
    }
}
