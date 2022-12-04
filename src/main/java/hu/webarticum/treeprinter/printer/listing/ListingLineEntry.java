package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.ConsoleText;
import hu.webarticum.treeprinter.util.Util;

public class ListingLineEntry {

    private final TreeNode node;

    private final ConsoleText liningPrefix;

    private final ConsoleText contentLine;

    private final AnsiMode ansiMode;

    public ListingLineEntry(TreeNode node, ConsoleText liningPrefix, ConsoleText contentLine, AnsiMode ansiMode) {
        this.node = node;
        this.liningPrefix = liningPrefix;
        this.contentLine = contentLine;
        this.ansiMode = ansiMode;
    }

    public TreeNode getNode() {
        return node;
    }

    public ConsoleText getLiningPrefix() {
        return liningPrefix;
    }

    public String getStringLiningPrefix() {
        return Util.getStringContent(liningPrefix, ansiMode);
    }

    public ConsoleText getContentLine() {
        return contentLine;
    }

    public String getStringContentLine() {
        return Util.getStringContent(contentLine, ansiMode);
    }
}
