package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.ConsoleText;

import java.util.ArrayList;
import java.util.List;

public class CollectorListingOutputSink implements ListingOutputSink {

    private final List<ListingLineEntry> output = new ArrayList<>();

    
    @Override
    public void writeln(TreeNode node, ConsoleText prefix, ConsoleText line) {
        output.add(new ListingLineEntry(node, prefix, line));
    }

    public List<ListingLineEntry> getOutput() {
        return output;
    }
}
