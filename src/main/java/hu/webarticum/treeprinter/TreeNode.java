package hu.webarticum.treeprinter;

import java.util.List;

public interface TreeNode {
    
    public String content();
    
    public List<TreeNode> children();
    
    public default TreeNode originalNode() {
        return this;
    }

    public default Insets insets() {
        return new Insets(0);
    }

    public default boolean isDecorable() {
        return true;
    }

    public default boolean isPlaceholder() {
        return false;
    }

}
