package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.TreeNode;

public interface ListingOutputSink {

    void writeln(TreeNode node, String prefix, String line);

}
