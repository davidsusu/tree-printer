package hu.webarticum.treeprinter.printer.traditional;

public class Align {

    private final int left;

    private final int topConnection;

    private final int bottomConnection;

    
    public Align(int left, int topConnection, int bottomConnection) {
        this.left = left;
        this.topConnection = topConnection;
        this.bottomConnection = bottomConnection;
    }

    
    public int left() {
        return left;
    }

    public int topConnection() {
        return topConnection;
    }

    public int bottomConnection() {
        return bottomConnection;
    }
    
}