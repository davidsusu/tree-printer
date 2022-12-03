package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.TreeNode;

public class ListingLineEntry {

    private final TreeNode node;

    private final String liningPrefix;

    private final String contentLine;

    public ListingLineEntry(TreeNode node, String liningPrefix, String contentLine) {
        this.node = node;
        this.liningPrefix = liningPrefix;
        this.contentLine = contentLine;
    }

    public TreeNode getNode() {
        return node;
    }

    public String getLiningPrefix() {
        return liningPrefix;
    }

    public String getContentLine() {
        return contentLine;
    }
}
