package hu.webarticum.treeprinter.printer.listing;

import hu.webarticum.treeprinter.AnsiMode;
import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.text.ConsoleText;
import hu.webarticum.treeprinter.util.Util;

public class AppendableListingOutputSink implements ListingOutputSink {

    private final Appendable out;

    private final AnsiMode ansiMode;

    public AppendableListingOutputSink(Appendable out, AnsiMode ansiMode) {
        this.out = out;
        this.ansiMode = ansiMode;
    }

    @Override
    public void writeln(TreeNode node, ConsoleText prefix, ConsoleText line) {
        Util.writeln(out, Util.getStringContent(prefix, ansiMode) + Util.getStringContent(line, ansiMode));
    }
}
