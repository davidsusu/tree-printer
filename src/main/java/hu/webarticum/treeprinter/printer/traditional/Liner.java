package hu.webarticum.treeprinter.printer.traditional;

import java.util.List;

import hu.webarticum.treeprinter.util.LineBuffer;

/**
 * Liner strategy interface for {@link TraditionalTreePrinter}
 */
public interface Liner {
    
    public int printConnections(LineBuffer buffer, int row, int topConnection, List<Integer> bottomConnections);
    
}