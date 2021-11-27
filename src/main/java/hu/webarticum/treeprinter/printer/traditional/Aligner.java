package hu.webarticum.treeprinter.printer.traditional;

import java.util.List;
import java.util.Map;

import hu.webarticum.treeprinter.TreeNode;

/**
 * Node aligner strategy interface for {@link TraditionalTreePrinter}
 */
public interface Aligner {
    
    public Align alignNode(TreeNode node, int position, int width, int contentWidth);
    
    public int[] alignChildren(TreeNode parentNode, List<TreeNode> children, int position, Map<TreeNode, Integer> widthMap);
    
    public int collectWidths(Map<TreeNode, Integer> widthMap, TreeNode node);
    
}