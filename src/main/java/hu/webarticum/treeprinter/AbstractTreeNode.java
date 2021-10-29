package hu.webarticum.treeprinter;

public abstract class AbstractTreeNode implements TreeNode {

    @Override
    public String toString() {
        return content();
    }
    
}
