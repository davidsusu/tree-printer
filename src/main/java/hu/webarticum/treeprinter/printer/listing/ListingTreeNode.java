package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.TreeNode;

public class ListingTreeNode {

    private final TreeNode node;

    private final String prefix;

    private final String line;

    public ListingTreeNode(TreeNode node, String prefix, String line) {
        this.node = node;
        this.prefix = prefix;
        this.line = line;
    }

    public TreeNode getNode() {
        return node;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getLine() {
        return line;
    }
}
