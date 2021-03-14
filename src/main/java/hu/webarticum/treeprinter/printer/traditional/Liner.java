package hu.webarticum.treeprinter.printer.traditional;

import java.util.List;

import hu.webarticum.treeprinter.util.LineBuffer;

public interface Liner {
    
    public int printConnections(LineBuffer buffer, int row, int topConnection, List<Integer> bottomConnections);
    
}