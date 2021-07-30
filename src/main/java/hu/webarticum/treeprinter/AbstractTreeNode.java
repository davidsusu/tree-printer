package hu.webarticum.treeprinter;

public abstract class AbstractTreeNode implements TreeNode {

    @Override
    public TreeNode getOriginalNode() {
        return this;
    }

    @Override
    public int[] getInsets() {
        return new int[] {0, 0, 0, 0};
    }

    @Override
    public boolean isDecorable() {
        return true;
    }

    @Override
    public boolean isPlaceholder() {
        return false;
    }

    @Override
    public String toString() {
        return getContent();
    }
    
}
