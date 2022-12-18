package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.ConsoleText;

public interface ListingOutputSink {

    void writeln(TreeNode node, ConsoleText prefix, ConsoleText line);

}
