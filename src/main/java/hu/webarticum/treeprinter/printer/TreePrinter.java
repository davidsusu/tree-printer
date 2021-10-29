package hu.webarticum.treeprinter.printer;

import hu.webarticum.treeprinter.TreeNode;

public interface TreePrinter {

    public void print(TreeNode rootNode, Appendable out);
    
    public default void print(TreeNode rootNode) {
        print(rootNode, System.out);
    }
    
    public default String stringify(TreeNode rootNode) {
        StringBuilder builder = new StringBuilder();
        print(rootNode, builder);
        return builder.toString();
    }
    
}
