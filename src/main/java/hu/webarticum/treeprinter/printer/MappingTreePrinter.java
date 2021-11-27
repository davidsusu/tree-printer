package hu.webarticum.treeprinter.printer;

import java.util.function.UnaryOperator;

import hu.webarticum.treeprinter.TreeNode;

/**
 * Helper implementation of {@link TreePrinter} that automatically applies a mapping
 */
public class MappingTreePrinter implements TreePrinter {
    
    private final TreePrinter baseTreePrinter;
    
    private final UnaryOperator<TreeNode> mapper;
    

    public MappingTreePrinter(TreePrinter baseTreePrinter, UnaryOperator<TreeNode> mapper) {
        this.baseTreePrinter = baseTreePrinter;
        this.mapper = mapper;
    }
    
    
    @Override
    public void print(TreeNode rootNode, Appendable out) {
        baseTreePrinter.print(mapper.apply(rootNode), out);
    }

}
