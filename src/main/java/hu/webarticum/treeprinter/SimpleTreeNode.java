package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple default implementation for {@link TreeNode}.
 * 
 * You can specify content and custom insets in the constructor.
 * Child nodes can be added after construction (so this class is partially mutable).
 */
public class SimpleTreeNode implements TreeNode {

    private final String content;
    
    private final Insets insets;
    
    private final List<TreeNode> children = new ArrayList<>();
    

    public SimpleTreeNode(String content) {
        this(content, Insets.EMPTY);
    }

    public SimpleTreeNode(String content, Insets insets) {
        this.content = content;
        this.insets = insets;
    }

    
    public void addChild(TreeNode childNode) {
        if (childNode == null) {
            throw new IllegalArgumentException("Child node must not be null, use placeholder instead");
        }
        
        children.add(childNode);
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public Insets insets() {
        return insets;
    }

    @Override
    public List<TreeNode> children() {
        return new ArrayList<>(children);
    }

}
