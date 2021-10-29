package hu.webarticum.treeprinter;

import java.util.List;

public interface TreeNode {
    
    public String content();
    
    public List<TreeNode> children();
    
    public default TreeNode originalNode() {
        return this;
    }

    public default Insets insets() {
        return Insets.EMPTY;
    }

    public default boolean isDecorable() {
        return true;
    }

    public default boolean isPlaceholder() {
        return false;
    }

}
