package hu.webarticum.treeprinter.decorator;

import hu.webarticum.treeprinter.TreeNode;

public class TrackingTreeNodeDecorator extends AbstractTreeNodeDecorator {

    public final TrackingTreeNodeDecorator parent;
    
    public final int index;
    

    public TrackingTreeNodeDecorator(TreeNode baseNode) {
        this(null, 0, baseNode);
    }
    
    public TrackingTreeNodeDecorator(TrackingTreeNodeDecorator parent, int index, TreeNode baseNode) {
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
        return new TrackingTreeNodeDecorator(this, index, childNode);
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
        if (!(other instanceof TrackingTreeNodeDecorator)) {
            return false;
        }

        TrackingTreeNodeDecorator otherReferenceTreeNode = (TrackingTreeNodeDecorator)other;
        TrackingTreeNodeDecorator otherParent = otherReferenceTreeNode.parent;
        
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
