package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.List;

import hu.webarticum.treeprinter.text.ConsoleText;

/**
 * Simple default implementation for {@link TreeNode}.
 * 
 * You can specify content and custom insets in the constructor.
 * Child nodes can be added after construction (so this class is partially mutable).
 */
public class SimpleTreeNode implements TreeNode {

    private final ConsoleText content;
    
    private final Insets insets;
    
    private final List<TreeNode> children = new ArrayList<>();
    

    public SimpleTreeNode() {
        this("");
    }
    
    public SimpleTreeNode(String content) {
        this(ConsoleText.of(content), Insets.EMPTY);
    }

    public SimpleTreeNode(String content, Insets insets) {
        this(ConsoleText.of(content), insets);
    }

    public SimpleTreeNode(ConsoleText content) {
        this(content, Insets.EMPTY);
    }

    public SimpleTreeNode(ConsoleText content, Insets insets) {
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
    public ConsoleText content() {
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
