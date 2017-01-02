package hu.webarticum.treeprinter;

import java.util.List;

public interface TreeNode {
	
	public String getContent();
	
	public TreeNode getOriginalNode();

	public int[] getInsets();
	
	public List<TreeNode> getChildren();

	public boolean isDecorable();
	
}
