package hu.webarticum.treeprinter;

import java.util.ArrayList;
import java.util.List;

public class SimpleTreeNode extends AbstractTreeNode {

    protected final String content;
    
    protected final int[] insets;
    
    protected List<TreeNode> children = new ArrayList<TreeNode>();

    public SimpleTreeNode(String content) {
        this(content, 0, 0, 0, 0);
    }

    public SimpleTreeNode(String content, int... insets) {
        this.content = content;
        this.insets = insets.clone();
    }

    public void addChild(TreeNode childNode) {
        children.add(childNode);
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public int[] getInsets() {
        return new int[] {insets[0], insets[1], insets[2], insets[3]};
    }

    @Override
    public List<TreeNode> getChildren() {
        return new ArrayList<TreeNode>(children);
    }

}
