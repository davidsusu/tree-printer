package hu.webarticum.treeprinter;

import java.util.List;

public interface TreeNode {
    
    public String getContent();
    
    public List<TreeNode> getChildren();
    
    public default TreeNode getOriginalNode() {
        return this;
    }

    public default Insets getInsets() {
        return new Insets(0);
    }

    public default boolean isDecorable() {
        return true;
    }

    public default boolean isPlaceholder() {
        return false;
    }

}
