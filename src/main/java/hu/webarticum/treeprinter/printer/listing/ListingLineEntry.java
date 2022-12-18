package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.ConsoleText;

public class ListingLineEntry {

    private final TreeNode node;

    private final ConsoleText liningPrefix;

    private final ConsoleText contentLine;

    public ListingLineEntry(TreeNode node, ConsoleText liningPrefix, ConsoleText contentLine) {
        this.node = node;
        this.liningPrefix = liningPrefix;
        this.contentLine = contentLine;
    }

    public TreeNode node() {
        return node;
    }

    public ConsoleText liningPrefix() {
        return liningPrefix;
    }

    public ConsoleText contentLine() {
        return contentLine;
    }
    
}
