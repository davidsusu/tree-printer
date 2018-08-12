package hu.webarticum.treeprinter;

public interface TreePrinter {
    
    public void print(TreeNode rootNode);
    
    public void print(TreeNode rootNode, Appendable out);
    
    public String getAsString(TreeNode rootNode);
    
}
