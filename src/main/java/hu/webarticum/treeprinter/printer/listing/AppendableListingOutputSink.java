package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.util.Util;

public class AppendableListingOutputSink implements ListingOutputSink {

    private final Appendable out;

    public AppendableListingOutputSink(Appendable out) {
        this.out = out;
    }

    @Override
    public void writeln(TreeNode node, String prefix, String line) {
        Util.writeln(out, prefix + line);
    }
}
