package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class CollectorListingOutputSink implements ListingOutputSink {

    private final List<ListingLineEntry> output = new ArrayList<>();

    @Override
    public void writeln(TreeNode node, String prefix, String line) {
        output.add(new ListingLineEntry(node, prefix, line));
    }

    public List<ListingLineEntry> getOutput() {
        return output;
    }
}
