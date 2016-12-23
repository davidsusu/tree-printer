package hu.webarticum.treeprinter;

public interface NodeDecorator {
	
	public String getDecoratedContent(TreeNode node);

	public int getOffset();
	
}
