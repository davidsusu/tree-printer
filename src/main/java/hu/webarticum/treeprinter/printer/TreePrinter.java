package hu.webarticum.treeprinter.printer;

import hu.webarticum.treeprinter.TreeNode;
import hu.webarticum.treeprinter.printer.listing.ListingTreePrinter;

/**
 * Main interface for tree printers. 
 * 
 * A tree printer visualize a {@link TreeNode} and its entire subtree structure.
 * Any implementation can have its own way to do this.
 * Some implementations (e. g. {@link ListingTreePrinter}) can print
 * large data in a memory efficient way via
 * <code>print(TreeNode)<code> or <code>print(TreeNode, Appendable)<code>.
 */
@FunctionalInterface
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
