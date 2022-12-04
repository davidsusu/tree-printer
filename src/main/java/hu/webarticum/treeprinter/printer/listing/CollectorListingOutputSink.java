package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.ConsoleText;

import java.util.ArrayList;
import java.util.List;

public class CollectorListingOutputSink implements ListingOutputSink {

    private final List<ListingLineEntry> output = new ArrayList<>();

    private final AnsiMode ansiMode;

    public CollectorListingOutputSink(AnsiMode ansiMode) {
        this.ansiMode = ansiMode;
    }

    @Override
    public void writeln(TreeNode node, ConsoleText prefix, ConsoleText line) {
        output.add(new ListingLineEntry(node, prefix, line, ansiMode));
    }

    public List<ListingLineEntry> getOutput() {
        return output;
    }
}
