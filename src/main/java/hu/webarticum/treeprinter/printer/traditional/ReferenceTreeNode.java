package hu.webarticum.treeprinter.printer.traditional;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.decorator.AbstractTreeNodeDecorator;

public class ReferenceTreeNode extends AbstractTreeNodeDecorator {

    public final ReferenceTreeNode parent;
    
    public final int index;
    

    public ReferenceTreeNode(TreeNode baseNode) {
        this(null, 0, baseNode);
    }
    
    public ReferenceTreeNode(ReferenceTreeNode parent, int index, TreeNode baseNode) {
        super(baseNode);
        this.parent = parent;
        this.index = index;
    }

    @Override
    public String getContent() {
        return decoratedNode.getContent();
    }

    @Override
    protected TreeNode decorateChild(TreeNode childNode, int index) {
        return new ReferenceTreeNode(this, index, childNode);
    }

    @Override
    public boolean isDecorable() {
        return false;
    }
    
    @Override
    public int hashCode() {
        int parentHashCode = parent != null ? parent.hashCode(): 0;
        return (parentHashCode * 37) + index;
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ReferenceTreeNode)) {
            return false;
        }

        ReferenceTreeNode otherReferenceTreeNode = (ReferenceTreeNode)other;
        ReferenceTreeNode otherParent = otherReferenceTreeNode.parent;
        
        if (parent == null) {
            if (otherParent != null) {
                return false;
            }
        } else if (otherParent == null || !parent.equals(otherParent)) {
            return false;
        }
        
        return index == otherReferenceTreeNode.index;
    }
    
}
